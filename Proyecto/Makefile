CC=gcc
CFLAGS=-Wall -std=c11
LDFLAGS=-lzmq

all: ps_client gc_broker actor_return actor_renew ga_storage

ps_client: src/ps_client.c src/common.c include/common.h
	$(CC) $(CFLAGS) -Iinclude $^ -o $@ $(LDFLAGS)

gc_broker: src/gc_broker.c src/common.c include/common.h
	$(CC) $(CFLAGS) -Iinclude $^ -o $@ $(LDFLAGS)

actor_return: src/actor_return.c src/common.c include/common.h
	$(CC) $(CFLAGS) -Iinclude $^ -o $@ $(LDFLAGS)

actor_renew: src/actor_renew.c src/common.c include/common.h
	$(CC) $(CFLAGS) -Iinclude $^ -o $@ $(LDFLAGS)

ga_storage: src/ga_storage.c src/common.c include/common.h
	$(CC) $(CFLAGS) -Iinclude $^ -o $@ $(LDFLAGS)

clean:
	rm -f ps_client gc_broker actor_return actor_renew ga_storage
