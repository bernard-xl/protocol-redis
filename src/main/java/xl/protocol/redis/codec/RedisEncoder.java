package xl.protocol.redis.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xl.protocol.redis.exception.RedisCodecException;
import xl.protocol.redis.message.*;
import xl.protocol.redis.util.Bytes;

import static xl.protocol.redis.codec.Protocol.*;

public class RedisEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        encode(msg, out);
    }

    private void encode(Message msg, ByteBuf out) {
        if (msg instanceof Simple) {
            encode((Simple)msg, out);
        } else if (msg instanceof Err) {
            encode((Err)msg, out);
        } else if (msg instanceof Bulk) {
            encode((Bulk)msg, out);
        } else if (msg instanceof Int) {
            encode((Int)msg, out);
        } else if (msg instanceof Array) {
            encode((Array)msg, out);
        } else {
            throw new RedisCodecException("unexpected message type: " + msg.getClass());
        }
    }

    private void encode(Simple msg, ByteBuf out) {
        out.writeByte(MARKER_SIMPLE);
        out.writeBytes(Bytes.of(msg.content()));
        out.writeBytes(CRLF);
    }

    private void encode(Err msg, ByteBuf out) {
        out.writeByte(MARKER_ERR);
        out.writeBytes(Bytes.of(msg.content()));
        out.writeBytes(CRLF);
    }

    private void encode(Int msg, ByteBuf out) {
        out.writeByte(MARKER_INT);
        out.writeBytes(Bytes.of(msg.content()));
        out.writeBytes(CRLF);
    }

    private void encode(Bulk msg, ByteBuf out) {
        out.writeByte(MARKER_BULK);
        out.writeBytes(Bytes.of(msg.length()));
        out.writeBytes(CRLF);
        if (msg.content() != null) {
            out.writeBytes(msg.content());
            out.writeBytes(CRLF);
        }
    }

    private void encode(Array msg, ByteBuf out) {
        out.writeByte(MARKER_ARRAY);
        out.writeBytes(Bytes.of(msg.length()));
        out.writeBytes(CRLF);
        if (msg.content() != null) {
            for (Message elem : msg.content()) {
                encode(elem, out);
            }
        }
    }
}
