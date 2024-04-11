package org.dronedudes.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	/*
		Tilgå DB på localhost:8080/h2-console
		JDBC URL = jdbc:h2:file:/SemPro/Blueprint/data
		Username = BlueprintDB
		Password = Intet password

<<<<<<< HEAD
		Gå til BlueprintController og uncomment for at generere entries i DB
	 */
=======
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
>>>>>>> Sprint-1
}
