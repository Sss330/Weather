package exception;

public class DeletingUserException extends RuntimeException {
    public DeletingUserException(String message) {
        super(message);
    }
}
