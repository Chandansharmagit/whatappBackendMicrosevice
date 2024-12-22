package authentications.authentications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class AuthenticationsApplication {

	

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationsApplication.class, args);
	}

}
