package springbook.exception.example.exception;

import java.io.IOException;

public class RetryFailedException extends RuntimeException {
    public RetryFailedException(String msg) {
        super(msg);
    }
}
