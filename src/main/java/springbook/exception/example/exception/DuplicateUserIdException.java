package springbook.exception.example.exception;

import java.sql.SQLException;

public class DuplicateUserIdException extends Exception {
    public DuplicateUserIdException() {}
    public DuplicateUserIdException(String msg) {
        super(msg);
    }
    public DuplicateUserIdException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public DuplicateUserIdException(Throwable cause) {
        super(cause);
    }
}
