package cn.hurrican.redis;

public class JedisRuntimeException extends RuntimeException {

    public JedisRuntimeException() {
    }

    public JedisRuntimeException(String message) {
        super(message);
    }

    public JedisRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JedisRuntimeException(Throwable cause) {
        super(cause);
    }
}
