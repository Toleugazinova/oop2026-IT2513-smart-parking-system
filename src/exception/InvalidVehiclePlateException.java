package exception;

public class InvalidVehiclePlateException extends RuntimeException {
    public InvalidVehiclePlateException() {
        super("Invalid or unknown vehicle plate");
    }
}