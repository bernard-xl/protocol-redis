Redis Serialization Protocol Client
===

This is a low-level Redis client that provides minimal abstraction over the Redis Serialization Protocol (RESP). 
Users could speak RESP directly to Redis via the client.

```java
RedisClient client = new RedisClient("localhost", 6379);
CompletableFuture<Message> reply = client.request(array(bulk("PING")));

System.out.println(reply.get()); //PONG
```
Some of the Redis commands are provided as well:
```java
// string commands
client.request(set("hello", "world"));
client.request(get("hello"));
client.request(append("hello", "!"));

// counter commands
client.request(incr("counter"));
client.request(incrBy("counter", 2));
```

