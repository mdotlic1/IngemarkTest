package com.ingemark.demo;

import com.ingemark.demo.model.User;
import com.ingemark.demo.repository.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Ingemark test",
				version = "1.0.0",
				description = "A RESTful API for basic CRUD operations on products.",
				contact = @Contact(
						name = "Milan Dotlić",
						email = "mdotlic@gmail.com"
				)
		)
)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


	@Bean
	CommandLineRunner commandLineRunner(UserRepository users, PasswordEncoder passwordEncoder){
		return args -> {
			if(users.findByUsername("admin").isEmpty()){
				users.save(new User("admin",passwordEncoder.encode("admin"),"ROLE_ADMIN,ROLE_USER"));
			}
			if(users.findByUsername("user").isEmpty()){
				users.save(new User("user",passwordEncoder.encode("user"),"ROLE_USER"));
			}
		};
	}

}
