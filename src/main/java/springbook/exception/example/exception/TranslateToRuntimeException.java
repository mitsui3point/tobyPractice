package springbook.exception.example.exception;

public class TranslateToRuntimeException extends RuntimeException {
    public TranslateToRuntimeException() {}
    public TranslateToRuntimeException(String msg) {
        super(msg);
    }
    public TranslateToRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public TranslateToRuntimeException(Throwable cause) {
        super(cause);
    }
}
