package pe.com.hotel_api.hotel.presentation.advice;

public class IllegalArgumentException extends RuntimeException{
    public IllegalArgumentException(String message) {
        super(message);
    }
}
