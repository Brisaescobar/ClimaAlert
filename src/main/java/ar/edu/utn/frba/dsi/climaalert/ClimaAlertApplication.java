package ar.edu.utn.frba.dsi.climaalert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClimaAlertApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClimaAlertApplication.class, args);
	}

}
