package xl.protocol.redis.util;

import java.nio.charset.StandardCharsets;

public class Bytes {
    private Bytes() {
    }

    public static byte[] of(int value) {
        return of(Integer.toString(value));
    }

    public static byte[] of(long value) {
        return of(Long.toString(value));
    }

    public static byte[] of(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
