package com.proyecto.vehiculos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling // Habilita las tareas programadas (Scheduler)
@SpringBootApplication
public class VehiculosApplication {

	public static void main(String[] args) {
        SpringApplication.run(VehiculosApplication.class, args);
        System.out.println(" Aplicación iniciada correctamente con tareas programadas habilitadas.");
	}
}
