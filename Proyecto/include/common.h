#ifndef COMMON_H
#define COMMON_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <zmq.h>

#define MAX_MSG 512
#define TOPIC_DEV "DEV"
#define TOPIC_REN "REN"

// Funciones auxiliares
static inline void die(const char *msg) {
    perror(msg);
    exit(EXIT_FAILURE);
}

static inline void *zmq_ctx_or_die() {
    void *ctx = zmq_ctx_new();
    if (!ctx) die("zmq_ctx_new");
    return ctx;
}

static inline void *zmq_socket_or_die(void *ctx, int type) {
    void *s = zmq_socket(ctx, type);
    if (!s) die("zmq_socket");
    return s;
}

static inline void zmq_bind_or_die(void *sock, const char *endpoint) {
    if (zmq_bind(sock, endpoint) != 0) die("zmq_bind");
}

static inline void zmq_connect_or_die(void *sock, const char *endpoint) {
    if (zmq_connect(sock, endpoint) != 0) die("zmq_connect");
}

static inline int zmq_recv_str(void *sock, char *buf, size_t buflen) {
    int n = zmq_recv(sock, buf, buflen - 1, 0);
    if (n >= 0) buf[n] = '\0';
    return n;
}

static inline void zmq_send_str(void *sock, const char *s) {
    zmq_send(sock, s, strlen(s), 0);
}

#endif
