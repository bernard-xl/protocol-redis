package xl.protocol.redis.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import xl.protocol.redis.message.*;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class RedisDecoderTest {

    @Test
    public void decodeSimple() {
        EmbeddedChannel channel = new EmbeddedChannel(new RedisDecoder());
        ByteBuf in = Unpooled.copiedBuffer("+OK\r\n", StandardCharsets.UTF_8);

        channel.writeInbound(in);

        assertEquals(new Simple("OK"), channel.readInbound());
    }

    @Test
    public void decodeErr() {
        EmbeddedChannel channel = new EmbeddedChannel(new RedisDecoder());
        ByteBuf in = Unpooled.copiedBuffer("-Ops\r\n", StandardCharsets.UTF_8);

        channel.writeInbound(in);

        assertEquals(new Err("Ops"), channel.readInbound());
    }

    @Test
    public void decodeInt() {
        EmbeddedChannel channel = new EmbeddedChannel(new RedisDecoder());
        ByteBuf in = Unpooled.copiedBuffer(":-1\r\n", StandardCharsets.UTF_8);

        channel.writeInbound(in);

        assertEquals(new Int(-1), channel.readInbound());
    }

    @Test
    public void decodeBulk() {
        EmbeddedChannel channel = new EmbeddedChannel(new RedisDecoder());
        ByteBuf in = Unpooled.copiedBuffer("$5\r\nHello\r\n", StandardCharsets.UTF_8);

        channel.writeInbound(in);

        byte[] hello = "Hello".getBytes(StandardCharsets.UTF_8);

        assertEquals(new Bulk(hello), channel.readInbound());
    }

    @Test
    public void decodeArray() {
        EmbeddedChannel channel = new EmbeddedChannel(new RedisDecoder());
        ByteBuf in = Unpooled.copiedBuffer("*1\r\n$5\r\nHello\r\n", StandardCharsets.UTF_8);

        channel.writeInbound(in);

        byte[] hello = "Hello".getBytes(StandardCharsets.UTF_8);
        Message[] elements = new Message[] { new Bulk(hello) };

        assertEquals(new Array(elements), channel.readInbound());
    }
}
