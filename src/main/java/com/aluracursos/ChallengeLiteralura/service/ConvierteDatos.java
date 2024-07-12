package com.aluracursos.ChallengeLiteralura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase){
        try {
            var datosAntes= mapper.readValue(json, clase);
            return mapper.readValue(json, clase);

        } catch (JsonMappingException e){
            throw new RuntimeException(e);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
