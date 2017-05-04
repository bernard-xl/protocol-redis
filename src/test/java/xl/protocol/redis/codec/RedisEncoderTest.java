package xl.protocol.redis.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import xl.protocol.redis.message.*;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class RedisEncoderTest {

    private static final String STRING = "Testing";

    private static final long INT = -1;

    private static final byte[] BYTES = new byte[] {7, 7, 7, 7};

    private RedisEncoder encoder = new RedisEncoder();

    @Test
    public void encodeSimple() {
        EmbeddedChannel channel = new EmbeddedChannel(encoder);
        channel.writeOutbound(new Simple(STRING));

        ByteBuf out = channel.readOutbound();
        assertEquals(Protocol.MARKER_SIMPLE, out.readByte());
        assertEquals(STRING, out.readSlice(STRING.length()).toString(StandardCharsets.UTF_8));
        assertEquals(Protocol.CR, out.readByte());
        assertEquals(Protocol.LF, out.readByte());

        assertEquals(0, out.readableBytes());
    }

    @Test
    public void encodeErr() {
        EmbeddedChannel channel = new EmbeddedChannel(encoder);
        channel.writeOutbound(new Err(STRING));

        ByteBuf out = channel.readOutbound();
        assertEquals(Protocol.MARKER_ERR, out.readByte());
        assertEquals(STRING, out.readSlice(STRING.length()).toString(StandardCharsets.UTF_8));
        assertEquals(Protocol.CR, out.readByte());
        assertEquals(Protocol.LF, out.readByte());

        assertEquals(0, out.readableBytes());
    }

    @Test
    public void encodeInt() {
        EmbeddedChannel channel = new EmbeddedChannel(encoder);
        channel.writeOutbound(new Int(INT));

        String encoded = Long.toString(INT);

        ByteBuf out = channel.readOutbound();
        assertEquals(Protocol.MARKER_INT, out.readByte());
        assertEquals(encoded, out.readSlice(encoded.length()).toString(StandardCharsets.UTF_8));
        assertEquals(Protocol.CR, out.readByte());
        assertEquals(Protocol.LF, out.readByte());

        assertEquals(0, out.readableBytes());
    }

    @Test
    public void encodeBulk() {
        EmbeddedChannel channel = new EmbeddedChannel(encoder);
        channel.writeOutbound(new Bulk(BYTES));

        String length = Integer.toString(BYTES.length);

        ByteBuf out = channel.readOutbound();
        assertEquals(Protocol.MARKER_BULK, out.readByte());
        assertEquals(length, out.readSlice(length.length()).toString(StandardCharsets.UTF_8));
        assertEquals(Protocol.CR, out.readByte());
        assertEquals(Protocol.LF, out.readByte());

        for (byte b : BYTES) {
            assertEquals(b, out.readByte());
        }

        assertEquals(Protocol.CR, out.readByte());
        assertEquals(Protocol.LF, out.readByte());

        assertEquals(0, out.readableBytes());
    }

    @Test
    public void encodeArray() {
        EmbeddedChannel channel = new EmbeddedChannel(encoder);
        Message[] elements = new Message[] {new Bulk(BYTES)};
        channel.writeOutbound(new Array(elements));

        String length = Integer.toString(elements.length);

        ByteBuf out = channel.readOutbound();
        assertEquals(Protocol.MARKER_ARRAY, out.readByte());
        assertEquals(length, out.readSlice(length.length()).toString(StandardCharsets.UTF_8));
        assertEquals(Protocol.CR, out.readByte());
        assertEquals(Protocol.LF, out.readByte());

        String bulkLength = Integer.toString(BYTES.length);

        assertEquals(Protocol.MARKER_BULK, out.readByte());
        assertEquals(bulkLength, out.readSlice(bulkLength.length()).toString(StandardCharsets.UTF_8));
        assertEquals(Protocol.CR, out.readByte());
        assertEquals(Protocol.LF, out.readByte());

        for (byte b : BYTES) {
            assertEquals(b, out.readByte());
        }

        assertEquals(Protocol.CR, out.readByte());
        assertEquals(Protocol.LF, out.readByte());

        assertEquals(0, out.readableBytes());
    }
}
