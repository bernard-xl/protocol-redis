package xl.protocol.redis.util;

import java.nio.charset.StandardCharsets;

public class Chars {
    private Chars() {
    }

    public static String of(byte[] value) {
        return new String(value, StandardCharsets.UTF_8);
    }
}
