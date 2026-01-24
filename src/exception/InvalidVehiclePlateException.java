package exception;

public class InvalidVehiclePlateException extends Exception {
    public InvalidVehiclePlateException(String message) {
        super(message);
    }
}