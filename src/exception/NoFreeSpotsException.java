package exception;

public class NoFreeSpotsException extends RuntimeException {
    public NoFreeSpotsException() {
        super("No free parking spots available");
    }
}
