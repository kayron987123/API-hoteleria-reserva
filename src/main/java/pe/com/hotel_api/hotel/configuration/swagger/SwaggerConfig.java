package pe.com.hotel_api.hotel.configuration.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "Hotel API",
                description = "APi for do reservations in a hotel",
                termsOfService = "www.hotel-api.com/terms-and-conditions",
                version = "1.0.0",
                contact = @Contact(
                        name = "Gad Alva",
                        url = "www.hotel-api.com",
                        email = "hotel@gmail.com"
                ),
                license = @License(
                        name = "Standard Software Use License for Hotel API",
                        url = "www.hotel-api.com/license"
                )
        ),
        servers = {
                @Server(
                        description = "Dev server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Prod server",
                        url = "https://hoteleria-cuh2gzacd2fkg5f9.mexicocentral-01.azurewebsites.net"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Acces Token for the API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
