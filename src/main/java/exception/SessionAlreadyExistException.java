package exception;

public class SessionAlreadyExistException extends RuntimeException {
    public SessionAlreadyExistException(String message) {
        super(message);
    }
}
