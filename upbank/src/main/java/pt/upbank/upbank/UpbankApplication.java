package pt.upbank.upbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UpbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpbankApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("abc123"));
	}

}
