package com.aluracursos.ChallengeLiteralura.repository;

import com.aluracursos.ChallengeLiteralura.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatosLibroRepository extends JpaRepository <Libro, Long> {
    boolean existsByTitulo(String titulo);
}
