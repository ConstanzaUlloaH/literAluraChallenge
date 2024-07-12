package com.aluracursos.ChallengeLiteralura;

import com.aluracursos.ChallengeLiteralura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.aluracursos.ChallengeLiteralura.repository.AutorRepository;
import com.aluracursos.ChallengeLiteralura.repository.DatosLibroRepository;

@SpringBootApplication
public class ChallengeLiteraluraApplication implements CommandLineRunner {
	@Autowired
	private DatosLibroRepository repository;

	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository, autorRepository);
		principal.muestraElMenu();
	}
}
