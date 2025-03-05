package exception;

public class SessionAlreadyDeadException extends RuntimeException {
    public SessionAlreadyDeadException(String message) {
        super(message);
    }
}
