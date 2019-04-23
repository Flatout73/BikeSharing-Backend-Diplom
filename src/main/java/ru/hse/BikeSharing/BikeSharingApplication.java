package ru.hse.BikeSharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BikeSharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeSharingApplication.class, args);
	}

}
