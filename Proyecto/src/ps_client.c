#include "common.h"

// Uso: ./ps_client tcp://127.0.0.1:5550 data/requests.txt

int main(int argc, char *argv[]) {
    if (argc < 3) {
        fprintf(stderr, "Uso: %s <gc_endpoint> <archivo_requerimientos>\n", argv[0]);
        return 1;
    }

    const char *gc_endpoint = argv[1];
    const char *file = argv[2];

    FILE *f = fopen(file, "r");
    if (!f) die("No pude abrir archivo de requests");

    void *ctx = zmq_ctx_or_die();
    void *req = zmq_socket_or_die(ctx, ZMQ_REQ);
    zmq_connect_or_die(req, gc_endpoint);

    char line[256];
    char reply[MAX_MSG];

    while (fgets(line, sizeof(line), f)) {
        // quitar salto de línea
        line[strcspn(line, "\r\n")] = 0;
        if (!*line) continue;

        printf("[PS] -> %s\n", line);
        zmq_send_str(req, line);

        zmq_recv_str(req, reply, sizeof(reply));
        printf("[PS] <- %s\n", reply);
    }

    fclose(f);
    zmq_close(req);
    zmq_ctx_term(ctx);
    return 0;
}
