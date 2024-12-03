package pe.com.hotel_api.hotel.presentation.advice;

public class SedeNotFoundException extends RuntimeException {
    public SedeNotFoundException(String message) {
        super(message);
    }
}
