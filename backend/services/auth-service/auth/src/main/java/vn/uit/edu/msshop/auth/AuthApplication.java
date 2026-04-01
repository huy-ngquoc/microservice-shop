package vn.uit.edu.msshop.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuthApplication {
	
	public static void main(String[] args) {
		//System.out.println("Mongo db uri "+mongoDBUri);
		SpringApplication.run(AuthApplication.class, args);
	}

}
