Redis Serialization Protocol Client
===

This is a low-level Redis client that provides minimal abstraction over the Redis Serialization Protocol (RESP). 
Users could use it to speak RESP directly to Redis.

```java
RedisClient client = new RedisClient("localhost", 6379);
client.request(array(bulk("PING")))
```