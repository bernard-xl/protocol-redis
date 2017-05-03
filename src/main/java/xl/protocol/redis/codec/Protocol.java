package xl.protocol.redis.codec;

import xl.protocol.redis.util.Bytes;

public class Protocol {

    public static final byte MARKER_SIMPLE = '+';
    public static final byte MARKER_ERR = '-';
    public static final byte MARKER_INT = ':';
    public static final byte MARKER_BULK = '$';
    public static final byte MARKER_ARRAY = '*';

    public static final byte[] CRLF = Bytes.of("\r\n");
    public static final byte CR = '\r';
    public static final byte LF = '\n';

    public static final int NULL_LENGTH = -1;
}
