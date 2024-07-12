package com.aluracursos.ChallengeLiteralura.repository;

import com.aluracursos.ChallengeLiteralura.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository <Autor, Long> {
}