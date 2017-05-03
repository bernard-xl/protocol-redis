package xl.protocol.redis.message;

import xl.protocol.redis.util.Bytes;

public interface Message {

    static Simple simple(String content) {
        return new Simple(content);
    }

    static Err err(String content) {
        return new Err(content);
    }

    static Int integer(long content) {
        return new Int(content);
    }

    static Bulk bulk(String content) {
        return new Bulk(Bytes.of(content));
    }

    static Bulk bulk(long content) {
        return new Bulk(Bytes.of(content));
    }

    static Bulk bulk(byte[] content) {
        return new Bulk(content);
    }

    static Array array(Message...content) {
        return new Array(content);
    }
}
