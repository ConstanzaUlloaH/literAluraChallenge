package com.aluracursos.ChallengeLiteralura.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
                        @JsonAlias("name")
                                 String nombre,

                         @JsonAlias("birth_year")
                                 Integer fechaNacimiento,

                         @JsonAlias("death_year")
                                 Integer fechaMuerte
) {}