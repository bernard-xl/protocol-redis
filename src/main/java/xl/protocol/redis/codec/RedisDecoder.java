package xl.protocol.redis.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ByteProcessor;
import xl.protocol.redis.exception.*;
import xl.protocol.redis.message.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static xl.protocol.redis.codec.Protocol.*;

public class RedisDecoder extends ByteToMessageDecoder {

    private static final int MAX_CONTENT_SIZE = 536870912;

    private State state = State.DECODE_TYPE;

    private int bulkLength = 0;

    private int arrayLength = -1;

    private ArrayList<Object> arrayElements = new ArrayList<>(4);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        List<Object> switchingOut = (arrayLength == -1)? out : arrayElements;

        switch (state) {
            case DECODE_TYPE:
                decodeType(in);
                break;
            case DECODE_SIMPLE_CONTENT:
                decodeSimpleContent(in, switchingOut);
                break;
            case DECODE_ERR_CONTENT:
                decodeErrContent(in, switchingOut);
                break;
            case DECODE_INT_CONTENT:
                decodeIntContent(in, switchingOut);
                break;
            case DECODE_BULK_HEADER:
                decodeBulkHeader(in, switchingOut);
                break;
            case DECODE_BULK_CONTENT:
                decodeBulkContent(in, switchingOut);
                break;
            case DECODE_ARRAY_HEADER:
                decodeArrayHeader(in, switchingOut);
                break;
        }

        if (arrayLength == arrayElements.size()) {
            aggregateArray(out);
        }
    }

    private void aggregateArray(List<Object> out) {
        Iterator<Object> iter = out.iterator();
        Integer length = (Integer) iter.next();
        Array msg = aggregateArrayHelper(iter, length);
        out.add(msg);
        arrayLength = -1;
    }

    private Array aggregateArrayHelper(Iterator<Object> iter, int length) {
        Message[] content = new Message[length];
        for (int i = 0; i < length; i++) {
            Object item = iter.next();
            if (item instanceof Integer) {
                content[i] = aggregateArrayHelper(iter, (Integer)item);
            } else if (item instanceof Message) {
                content[i] = (Message)item;
            } else {
                throw new RedisCodecException("unexpected message type: " + item.getClass());
            }
        }
        return new Array(content);
    }

    private void decodeType(ByteBuf in) {
        if (in.readableBytes() < 1) {
            return;
        }

        switch (in.readByte()) {
            case MARKER_SIMPLE:
                state = State.DECODE_SIMPLE_CONTENT;
                break;
            case MARKER_ERR:
                state = State.DECODE_ERR_CONTENT;
                break;
            case MARKER_INT:
                state = State.DECODE_INT_CONTENT;
                break;
            case MARKER_BULK:
                state = State.DECODE_BULK_HEADER;
                break;
            case MARKER_ARRAY:
                state = State.DECODE_ARRAY_HEADER;
                break;
        }
    }

    private void decodeSimpleContent(ByteBuf in, List<Object> out) {
        ByteBuf line = extractLine(in);
        if (line == null) {
            return;
        }
        Simple msg = new Simple(line.toString(StandardCharsets.UTF_8));
        out.add(msg);
        state = State.DECODE_TYPE;
    }

    private void decodeErrContent(ByteBuf in, List<Object> out) {
        ByteBuf line = extractLine(in);
        if (line == null) {
            return;
        }
        Err msg = new Err(line.toString(StandardCharsets.UTF_8));
        out.add(msg);
        state = State.DECODE_TYPE;
    }

    private void decodeIntContent(ByteBuf in, List<Object> out) {
        ByteBuf line = extractLine(in);
        if (line == null) {
            return;
        }
        long content = Long.parseLong(line.toString(StandardCharsets.UTF_8));
        Int msg = new Int(content);
        out.add(msg);
        state = State.DECODE_TYPE;
    }

    private void decodeBulkHeader(ByteBuf in, List<Object> out) {
        ByteBuf line = extractLine(in);
        if (line == null) {
            return;
        }
        bulkLength = Integer.parseInt(line.toString(StandardCharsets.UTF_8));
        if (bulkLength == NULL_LENGTH) {
            out.add(Bulk.NULL);
            state = State.DECODE_TYPE;
        } else {
            state = State.DECODE_BULK_CONTENT;
        }
    }

    private void decodeBulkContent(ByteBuf in, List<Object> out) {
        if (in.readableBytes() > MAX_CONTENT_SIZE) {
            // TODO: throw
        }

        if (in.readableBytes() < bulkLength + 2) {
            return;
        }

        byte[] content = new byte[bulkLength];
        in.readBytes(content);
        out.add(new Bulk(content));

        in.readShort();
        state = State.DECODE_TYPE;
        bulkLength = 0;
    }

    private void decodeArrayHeader(ByteBuf in, List<Object> out) {
        ByteBuf line = extractLine(in);
        if (line == null) {
            return;
        }
        int length = Integer.parseInt(line.toString(StandardCharsets.UTF_8));
        if (length == -1) {
            out.add(Array.NULL);
            state = State.DECODE_TYPE;
        } else {
            out.add(length);
            state = State.DECODE_TYPE;
            arrayLength += length + 1;
        }
    }

    private ByteBuf extractLine(ByteBuf in) {
        int lfIdx = in.forEachByte(ByteProcessor.FIND_LF);
        if (lfIdx == -1) {
            if (in.readableBytes() > MAX_CONTENT_SIZE) {
                // TODO: throw
            }
            return null;
        }
        if (in.getByte(lfIdx - 1) != CR) {
            return null;
        }
        ByteBuf line = in.readSlice(lfIdx - in.readerIndex() - 1);
        in.readShort();
        return line;
    }

    enum State {
        DECODE_TYPE,
        DECODE_SIMPLE_CONTENT,
        DECODE_ERR_CONTENT,
        DECODE_INT_CONTENT,
        DECODE_BULK_HEADER,
        DECODE_BULK_CONTENT,
        DECODE_ARRAY_HEADER,
    }
}
