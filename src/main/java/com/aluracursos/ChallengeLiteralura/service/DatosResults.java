package com.aluracursos.ChallengeLiteralura.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosResults(
        @JsonAlias("results")
        List<DatosLibros> datosLibros

) {
}
