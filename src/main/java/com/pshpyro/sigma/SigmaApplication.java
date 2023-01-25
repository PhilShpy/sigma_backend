package com.pshpyro.sigma;

import com.pshpyro.sigma.user.entity.User;
import com.pshpyro.sigma.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SigmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SigmaApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserService userService) {
		return args -> {
			User user = new User(
					"user",
					"user@email.com",
					"$2a$10$NZqqAh/K/qnT1ZDpYQUL9.INu6of.dlrgb37OwzP.IpEnW5qyDXii"
			);
			userService.createUser(user);
		};
	}
}
