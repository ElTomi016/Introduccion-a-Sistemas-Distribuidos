#include "common.h"

// Uso: ./actor_renew tcp://127.0.0.1:5551

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Uso: %s <gc_pub>\n", argv[0]);
        return 1;
    }

    const char *gc_pub = argv[1];
    void *ctx = zmq_ctx_or_die();
    void *sub = zmq_socket_or_die(ctx, ZMQ_SUB);

    zmq_connect_or_die(sub, gc_pub);
    zmq_setsockopt(sub, ZMQ_SUBSCRIBE, TOPIC_REN, strlen(TOPIC_REN));

    while (1) {
        char topic[32], payload[MAX_MSG];
        zmq_recv_str(sub, topic, sizeof(topic));
        zmq_recv_str(sub, payload, sizeof(payload));

        printf("[ACTOR REN] <- %s | %s\n", topic, payload);
        // Aquí simularías actualización de BD
    }

    zmq_close(sub);
    zmq_ctx_term(ctx);
    return 0;
}
