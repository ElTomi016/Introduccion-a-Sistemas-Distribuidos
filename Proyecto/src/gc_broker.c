#include "common.h"

// Uso: ./gc_broker tcp://*:5550 tcp://*:5551

int main(int argc, char *argv[]) {
    if (argc < 3) {
        fprintf(stderr, "Uso: %s <bind_req> <bind_pub>\n", argv[0]);
        return 1;
    }

    const char *bind_req = argv[1]; // para PS
    const char *bind_pub = argv[2]; // para Actores

    void *ctx = zmq_ctx_or_die();
    void *rep_ps = zmq_socket_or_die(ctx, ZMQ_REP);
    zmq_bind_or_die(rep_ps, bind_req);

    void *pub = zmq_socket_or_die(ctx, ZMQ_PUB);
    zmq_bind_or_die(pub, bind_pub);

    printf("[GC] Esperando solicitudes en %s\n", bind_req);

    char buf[MAX_MSG], ack[MAX_MSG];

    while (1) {
        zmq_recv_str(rep_ps, buf, sizeof(buf));

        if (strncmp(buf, "DEV", 3) == 0) {
            snprintf(ack, sizeof(ack), "OK|DEV");
            zmq_send_str(rep_ps, ack);

            zmq_send(pub, TOPIC_DEV, strlen(TOPIC_DEV), ZMQ_SNDMORE);
            zmq_send(pub, buf, strlen(buf), 0);

            printf("[GC] recibió DEV: %s → respondió %s y publicó\n", buf, ack);
        }
        else if (strncmp(buf, "REN", 3) == 0) {
            snprintf(ack, sizeof(ack), "OK|REN");
            zmq_send_str(rep_ps, ack);

            zmq_send(pub, TOPIC_REN, strlen(TOPIC_REN), ZMQ_SNDMORE);
            zmq_send(pub, buf, strlen(buf), 0);

            printf("[GC] recibió REN: %s → respondió %s y publicó\n", buf, ack);
        }
        else {
            snprintf(ack, sizeof(ack), "ERR|op_invalida");
            zmq_send_str(rep_ps, ack);

            printf("[GC] recibió desconocido: %s → respondió error\n", buf);
        }
    }

    zmq_close(rep_ps);
    zmq_close(pub);
    zmq_ctx_term(ctx);
    return 0;
}
