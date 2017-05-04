package xl.protocol.redis.client;

import org.junit.Test;
import xl.protocol.redis.message.Message;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static xl.protocol.redis.client.RedisCommand.*;
import static xl.protocol.redis.message.Message.*;

public class RedisClientIntegrationTest {

    private static final RedisClient client = new RedisClient("127.0.0.1", 6379);

    @Test
    public void testPing() throws Exception {
        Message reply = client.request(ping()).get();
        assertThat(reply, is(simple("PONG")));
    }

    @Test
    public void testGetAndSet() throws Exception {
        String hello = "hello-" + System.currentTimeMillis();

        Message setReply = client.request(set(hello, "world")).get();
        assertThat(setReply, is(simple("OK")));

        Message getReply = client.request(get(hello)).get();
        assertThat(getReply, is(bulk("world")));

        Message getSetReply = client.request(getSet(hello, "redis")).get();
        assertThat(getSetReply, is(bulk("world")));

        Message getReply2 = client.request(get(hello)).get();
        assertThat(getReply2, is(bulk("redis")));
    }

    @Test
    public void testIncrAndDesc() throws Exception {
        String count = "count-" + System.currentTimeMillis();

        Message incrReply = client.request(incr(count)).get();
        assertThat(incrReply, is(integer(1)));

        Message incrByReply = client.request(incrBy(count, 3)).get();
        assertThat(incrByReply, is(integer(4)));

        Message decrReply = client.request(decr(count)).get();
        assertThat(decrReply, is(integer(3)));

        Message decrByReply = client.request(decrBy(count, 3)).get();
        assertThat(decrByReply, is(integer(0)));
    }
}
