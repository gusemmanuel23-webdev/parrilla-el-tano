package com.reservas.eltano;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class EltanoApplication {

	@PostConstruct
	public void init() {
		// Esta línea asegura que toda la lógica de fechas de Java use la hora de Argentina
		TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
	}

	public static void main(String[] args) {
		SpringApplication.run(EltanoApplication.class, args);
	}
}