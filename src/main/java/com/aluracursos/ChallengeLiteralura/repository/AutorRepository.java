package com.aluracursos.ChallengeLiteralura.repository;

import com.aluracursos.ChallengeLiteralura.Libros.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository <Autor, Long> {
//    List<Autor> findByFechaFallecimientoIsNullOrFechaFallecimientoGreaterThan(int anio);

//    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    @Query("SELECT a FROM Autor a where a.fechaFallecimiento is not null and a.fechaFallecimiento BETWEEN :anio AND :anio+100")
    List<Autor> selectAutoresVivos(int anio);

}