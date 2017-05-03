package xl.protocol.redis.exception;

public class RedisCodecException extends RuntimeException {
    public RedisCodecException() {
    }

    public RedisCodecException(String message) {
        super(message);
    }

    public RedisCodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisCodecException(Throwable cause) {
        super(cause);
    }

    public RedisCodecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
