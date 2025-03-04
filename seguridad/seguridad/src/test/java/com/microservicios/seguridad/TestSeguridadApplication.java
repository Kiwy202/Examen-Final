package com.microservicios.seguridad;

import org.springframework.boot.SpringApplication;

public class TestSeguridadApplication {

	public static void main(String[] args) {
		SpringApplication.from(SeguridadApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
