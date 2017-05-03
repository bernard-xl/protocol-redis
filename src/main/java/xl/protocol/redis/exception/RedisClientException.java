package xl.protocol.redis.exception;

public class RedisClientException extends RuntimeException {
    public RedisClientException() {
    }

    public RedisClientException(String message) {
        super(message);
    }

    public RedisClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisClientException(Throwable cause) {
        super(cause);
    }

    public RedisClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
