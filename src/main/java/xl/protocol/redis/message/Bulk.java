package xl.protocol.redis.message;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Bulk implements Message {

    public static final Bulk NULL = new Bulk(null);

    private byte[] content;

    public Bulk(byte[] content) {
        this.content = content;
    }

    public int length() {
        return (content == null)? -1 : content.length;
    }

    public byte[] content() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bulk bulk = (Bulk) o;
        return Arrays.equals(content, bulk.content);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(content);
    }

    @Override
    public String toString() {
        return new String(content, StandardCharsets.UTF_8);
    }
}
