package pe.com.hotel_api.hotel.presentation.advice;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message) {
        super(message);
    }
}
