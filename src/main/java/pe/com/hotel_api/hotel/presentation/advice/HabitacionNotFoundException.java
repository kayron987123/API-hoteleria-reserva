package pe.com.hotel_api.hotel.presentation.advice;

public class HabitacionNotFoundException extends RuntimeException{
    public HabitacionNotFoundException(String message) {
        super(message);
    }
}
