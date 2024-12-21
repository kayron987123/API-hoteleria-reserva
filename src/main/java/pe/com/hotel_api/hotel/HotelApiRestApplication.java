package pe.com.hotel_api.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HotelApiRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelApiRestApplication.class, args);
	}

}
