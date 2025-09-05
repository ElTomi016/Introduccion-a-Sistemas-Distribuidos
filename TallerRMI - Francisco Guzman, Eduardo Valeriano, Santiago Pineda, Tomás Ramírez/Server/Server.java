/*#######################################################################################
 #* Fecha: 05/09/2025
 #* Autor: Francisco Guzman, Santiago Pineda, Tomas Roa, Eduardo Valeriano
 #* Materia: Introduccion a los Sistemas Distribuidos
 #* Tema: Servidor RMI que administra la biblioteca, gestiona prestamos y devoluciones
 #*       de libros usando un archivo CSV como base de datos.
######################################################################################*/
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// ====== Interfaz remota ======
// Define el contrato que cliente y servidor comparten.
// Todos los metodos deben declarar RemoteException.
interface IBibliotecaRemote extends Remote {
    long registrarCliente(String sessionKey, String alias) throws RemoteException;

    RespuestaPrestamo prestamoPorIsbn(long clientId, String isbn) throws RemoteException;
    RespuestaPrestamo prestamoPorTitulo(long clientId, String titulo) throws RemoteException;
    String consulta(long clientId, String isbn) throws RemoteException;
    boolean devolucion(long clientId, String isbn) throws RemoteException;
}

// ====== DTO ======
// Objeto simple y serializable para devolver el resultado de las operaciones.
// Incluye un codigo de estado, un mensaje y la fecha de devolucion cuando aplica.
class RespuestaPrestamo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private boolean aprobado;
    private String codigo;       // OK, NOT_FOUND, NO_STOCK, MULTIPLE_CHOICES
    private String mensaje;
    private java.time.LocalDate fechaDevolucion;

    public RespuestaPrestamo(boolean a, String c, String m, java.time.LocalDate f) {
        this.aprobado = a; this.codigo = c; this.mensaje = m; this.fechaDevolucion = f;
    }
    public boolean isAprobado(){ return aprobado; }
    public String getCodigo(){ return codigo; }
    public String getMensaje(){ return mensaje; }
    public java.time.LocalDate getFechaDevolucion(){ return fechaDevolucion; }

    @Override public String toString() {
        return "aprobado=" + aprobado + ", codigo=" + codigo + ", mensaje=" + mensaje +
               (aprobado && fechaDevolucion!=null ? (", fechaDevolucion=" + fechaDevolucion) : "");
    }
}

// ====== Implementacion con CSV + IDs por sesion y logs cortos ======
// Implementa la interfaz remota y publica el objeto en un puerto especifico.
// La "base de datos" es un CSV sencillo con columnas: isbn,titulo,total,prestados.
class BibliotecaRemoteImpl extends UnicastRemoteObject implements IBibliotecaRemote {
    private static final Path CSV_PATH = Paths.get("dat", "libros.csv"); // isbn,titulo,total,prestados
    private static final int OBJ_PORT = 2001; // puerto del objeto remoto; abrir en firewall si se usa por LAN

    // Lock RW para coordinar lecturas y escrituras seguras sobre el CSV
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    // Mapa para asignar un id estable por sessionKey mientras el servidor viva
    private final ConcurrentHashMap<String, Long> sessionKeyToId = new ConcurrentHashMap<>();
    // Secuencia atomica para generar nuevos ids de cliente
    private final AtomicLong seq = new AtomicLong(1);

    // Exporta el objeto remoto en el puerto OBJ_PORT
    protected BibliotecaRemoteImpl() throws RemoteException { super(OBJ_PORT); }

    // ===== Utils =====
    // Crea el directorio y archivo CSV si no existen, con datos de ejemplo
    private void ensureCsv() {
        try {
            Files.createDirectories(CSV_PATH.getParent());
            if (!Files.exists(CSV_PATH)) {
                try (BufferedWriter bw = Files.newBufferedWriter(CSV_PATH)) {
                    bw.write("9780001,Algoritmos,3,1\n");
                    bw.write("9780002,Sistemas Distribuidos,2,0\n");
                    bw.write("9780003,Redes,1,0\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear dat/libros.csv", e);
        }
    }
    // Calcula ejemplares disponibles = total - prestados
    private static int disponibles(String[] row){
        int total = Integer.parseInt(row[2]);
        int prest = Integer.parseInt(row[3]);
        return total - prest;
    }
    // Carga el CSV completo en memoria a un mapa (clave = isbn)
    private Map<String, String[]> cargar() {
        ensureCsv();
        lock.readLock().lock();
        try (BufferedReader br = Files.newBufferedReader(CSV_PATH)) {
            Map<String, String[]> m = new HashMap<>();
            String l;
            while ((l = br.readLine()) != null) {
                if (l.isBlank()) continue;
                String[] p = l.split(",", -1);
                m.put(p[0].trim(), new String[]{p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim()});
            }
            return m;
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo CSV", e);
        } finally { lock.readLock().unlock(); }
    }
    // Persiste el mapa completo de vuelta al CSV, sanitizando comas en el titulo
    private void guardar(Map<String, String[]> m) {
        lock.writeLock().lock();
        try (BufferedWriter bw = Files.newBufferedWriter(
                CSV_PATH,
                java.nio.charset.StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {
            for (String[] r : m.values()) {
                String tituloSeguro = r[1].replace(",", " ");
                bw.write(String.join(",", r[0], tituloSeguro, r[2], r[3]));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error guardando CSV", e);
        } finally { lock.writeLock().unlock(); }
    }
    // Marca de tiempo para logs
    private String stamp(){ return LocalDateTime.now().toString(); }
    // Log simple de operaciones del servidor
    private void log(long id, String accion, String resultado) {
        System.out.println("[SERVIDOR] " + stamp() + " | Cliente #" + id + " | " + accion + " -> " + resultado);
    }

    // ===== Registro de cliente (IDs por sesion del servidor) =====
    // Asigna un id por sessionKey; si ya existe, lo reutiliza.
    @Override
    public long registrarCliente(String sessionKey, String alias) throws RemoteException {
        if (sessionKey == null || sessionKey.isBlank()) sessionKey = "anon";
        Long id = sessionKeyToId.get(sessionKey);
        if (id == null) {
            id = seq.getAndIncrement();
            sessionKeyToId.put(sessionKey, id);
            System.out.println("[SERVIDOR] " + stamp() + " | Cliente #" + id + " | conectado");
        } else {
            System.out.println("[SERVIDOR] " + stamp() + " | Cliente #" + id + " | reconectado");
        }
        return id;
    }

    // ===== Metodos del taller (reciben clientId solo para loguear) =====
    // Consulta por ISBN: devuelve datos y disponibilidad si existe, o NOT_FOUND
    @Override
    public String consulta(long clientId, String isbn) throws RemoteException {
        Objects.requireNonNull(isbn, "isbn");
        isbn = isbn.trim();

        lock.readLock().lock();
        try {
            Map<String, String[]> m = cargar();
            String[] r = m.get(isbn);
            String resp = (r == null)
                    ? "NOT_FOUND | No existe el libro con ISBN " + isbn
                    : "OK | Libro: " + r[1] + " | Disponibles: " + disponibles(r) + " | Total: " + r[2];
            log(clientId, "consulta", (r == null ? "NOT_FOUND" : "OK"));
            return resp;
        } finally { lock.readLock().unlock(); }
    }

    // Prestamo por ISBN: valida existencia y stock, actualiza CSV y retorna fecha de devolucion
    @Override
    public RespuestaPrestamo prestamoPorIsbn(long clientId, String isbn) throws RemoteException {
        Objects.requireNonNull(isbn, "isbn");
        isbn = isbn.trim();

        lock.writeLock().lock();
        try {
            Map<String, String[]> m = cargar();
            String[] r = m.get(isbn);
            RespuestaPrestamo out;
            if (r == null) {
                out = new RespuestaPrestamo(false, "NOT_FOUND", "ISBN no encontrado", null);
            } else if (disponibles(r) <= 0) {
                out = new RespuestaPrestamo(false, "NO_STOCK", "Sin ejemplares disponibles", null);
            } else {
                // Incrementa prestados y guarda
                r[3] = String.valueOf(Integer.parseInt(r[3]) + 1);
                m.put(isbn, r);
                guardar(m);
                LocalDate fecha = LocalDate.now().plusWeeks(1);
                out = new RespuestaPrestamo(true, "OK", "Prestamo aprobado", fecha);
            }
            log(clientId, "prestamoPorIsbn", out.getCodigo());
            return out;
        } finally { lock.writeLock().unlock(); }
    }

    // Prestamo por titulo: puede arrojar NOT_FOUND, MULTIPLE_CHOICES, NO_STOCK u OK
    @Override
    public RespuestaPrestamo prestamoPorTitulo(long clientId, String titulo) throws RemoteException {
        Objects.requireNonNull(titulo, "titulo");
        titulo = titulo.trim();

        lock.writeLock().lock();
        try {
            Map<String, String[]> m = cargar();
            List<String[]> matches = new ArrayList<>();
            for (String[] r : m.values()) if (r[1].equalsIgnoreCase(titulo)) matches.add(r);

            RespuestaPrestamo out;
            if (matches.isEmpty())  {
                out = new RespuestaPrestamo(false, "NOT_FOUND", "Titulo no encontrado", null);
            } else if (matches.size() > 1) {
                // Construye sugerencias de ISBN cuando hay mas de una coincidencia
                String sug = "";
                for (String[] r : matches) sug += r[0] + " (" + r[1] + "), ";
                sug = sug.replaceAll(", $", "");
                out = new RespuestaPrestamo(false, "MULTIPLE_CHOICES",
                        "Hay multiples libros con ese titulo. Especifique ISBN: " + sug, null);
            } else {
                String[] r = matches.get(0);
                if (disponibles(r) <= 0) {
                    out = new RespuestaPrestamo(false, "NO_STOCK", "Sin ejemplares disponibles", null);
                } else {
                    // Incrementa prestados y guarda
                    r[3] = String.valueOf(Integer.parseInt(r[3]) + 1);
                    m.put(r[0], r);
                    guardar(m);
                    LocalDate fecha = LocalDate.now().plusWeeks(1);
                    out = new RespuestaPrestamo(true, "OK", "Prestamo aprobado", fecha);
                }
            }
            log(clientId, "prestamoPorTitulo", out.getCodigo());
            return out;
        } finally { lock.writeLock().unlock(); }
    }

    // Devolucion por ISBN: decrementa prestados si hay alguno y persiste
    @Override
    public boolean devolucion(long clientId, String isbn) throws RemoteException {
        Objects.requireNonNull(isbn, "isbn");
        isbn = isbn.trim();

        lock.writeLock().lock();
        try {
            Map<String, String[]> m = cargar();
            String[] r = m.get(isbn);
            boolean ok = false;
            if (r != null) {
                int prest = Integer.parseInt(r[3]);
                if (prest > 0) {
                    r[3] = String.valueOf(prest - 1);
                    m.put(isbn, r);
                    guardar(m);
                    ok = true;
                }
            }
            log(clientId, "devolucion", ok ? "OK" : "FAIL");
            return ok;
        } finally { lock.writeLock().unlock(); }
    }
}

// ====== Main del Servidor ======
// Inicia (o reutiliza) el Registry en el puerto 1099, crea la instancia del servicio
// y la publica bajo el nombre "BibliotecaService". El objeto remoto escucha en 2001.
public class Server {
    public static void main(String[] args) throws Exception {
        int registryPort = 1099;
        try {
            LocateRegistry.createRegistry(registryPort);
            System.out.println("Registry RMI creado en " + registryPort);
        } catch (Exception e) {
            System.out.println("Usando Registry existente en " + registryPort);
        }
        IBibliotecaRemote svc = new BibliotecaRemoteImpl();
        Registry reg = LocateRegistry.getRegistry(registryPort);
        reg.rebind("BibliotecaService", svc);
        System.out.println("Servicio 'BibliotecaService' publicado (objeto en 2001). Listo para clientes.");
    }
}