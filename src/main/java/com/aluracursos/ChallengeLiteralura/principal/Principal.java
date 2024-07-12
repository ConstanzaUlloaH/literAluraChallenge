package com.aluracursos.ChallengeLiteralura.principal;

import com.aluracursos.ChallengeLiteralura.Autor;
import com.aluracursos.ChallengeLiteralura.Libro;
import com.aluracursos.ChallengeLiteralura.service.*;
import com.aluracursos.ChallengeLiteralura.repository.AutorRepository;
import com.aluracursos.ChallengeLiteralura.repository.DatosLibroRepository;

import java.util.Scanner;

public class Principal {
    private ConsultaAPI consultaAPI = new ConsultaAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    Scanner lectura = new Scanner(System.in);
    private ConvierteDatos conversor = new ConvierteDatos();
    private DatosLibroRepository repositorio;
    private AutorRepository autorRepositorio;

    public Principal(DatosLibroRepository repository, AutorRepository autorRepositorio) {
        this.repositorio = repository;
        this.autorRepositorio = autorRepositorio;
    }


    public void muestraElMenu(){
//        var json = consultaAPI.obtenerDatos(URL_BASE);
//        System.out.println(json);

        var opcion = -1;
        while (opcion != 0){
            var menu= """
                    1 - buscar libros por titulo
                    2 - mostrar libros registrados
                    3 - mostrar autores registrados
                    4 - mostrar autores vivos en un determinado año
                    5 - mostrar libros por idioma
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = lectura.nextInt();
            lectura.nextLine();

            switch (opcion){
                case 1:
                    // 1. Buscar libros segun query/input usuario
                    buscarLibrosPorTitulo();

                    // 2. Si no existe el libro en la base de datos, Insertar.


                    // 3. Buscar el libro en la base de datos y mostrarlo en consola

                    break;
//                case 2:
//                    MostrarLibrosRegistrados();
//                    break;
//                case 3:
//                    MostrarAutoresRegistrados();
//                    break;
//                case 4:
//                    MostrarAutoresVivos();
//                    break;
//                case 5:
//                    MostrarLibrosPorIdioma();
//                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion...");
                    break;
                default:
                    System.out.println("Opcion invalida");


            }



        }

    }

    private DatosResults getDatosLibros() {
        System.out.println("Escribe el libro que deseas buscar");
        var nombreLibro = lectura.nextLine().trim();
        var json = consultaAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "%20"));
        DatosResults datosResults = conversor.obtenerDatos(json, DatosResults.class);
        return datosResults;

    }

    private void buscarLibrosPorTitulo(){
        //record
        DatosLibros datosLibros = getDatosLibros().datosLibros().stream()
                // Filtramos los libros segun el que tenga mayor numero de descargas.
                .reduce((datosLibros1, datosLibros2) -> {
                    boolean Libro2EsMayorQueLibro1 = datosLibros2.numeroDescargas() > datosLibros1.numeroDescargas();
                    return Libro2EsMayorQueLibro1 ? datosLibros2 : datosLibros1;
                }).get();

        // Validar que el libro no exista antes de guardarlo
        boolean existeLibro = repositorio.existsByTitulo(datosLibros.titulo());

        if (!existeLibro){
            // Guardar el autor/libro si el libro no existe en la base de datos
            Libro libro2 = new Libro(datosLibros);
            DatosAutor datosAutor = new DatosAutor(
                    libro2.getAutor().getNombre(),
                    libro2.getAutor().getFechaNacimiento(),
                    libro2.getAutor().getFechaFallecimiento());

            Autor autor = autorRepositorio.save(new Autor(datosAutor));
            libro2.setAutor(autor);
            repositorio.save(libro2);
            System.out.println("Se ha encontrado y registrado el siguiente libro:  " + datosLibros.toString());
        } else {
            System.out.println("Se ha encontrado el siguiente registro:  " + datosLibros.toString());
        }





    }

}
