package com.aluracursos.ChallengeLiteralura.principal;

import com.aluracursos.ChallengeLiteralura.Libros.*;
import com.aluracursos.ChallengeLiteralura.service.*;
import com.aluracursos.ChallengeLiteralura.repository.AutorRepository;
import com.aluracursos.ChallengeLiteralura.repository.DatosLibroRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

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

        int opcion = -1;
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
            System.out.print("Ingrese una opción: ");

            String opcionInput = lectura.nextLine();

            try {
                opcion = Integer.parseInt(opcionInput);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.\n\n");
                continue;
            }

            switch (opcion){
                case 1:
                    // 1. Buscar libros segun query/input usuario
                    buscarLibrosPorTitulo();
                    continue;
                case 2:
                    mostrarLibrosRegistrados();
                    continue;
                case 3:
                    mostrarAutoresRegistrados();
                    continue;
                case 4:
                    mostrarAutoresVivosSegunAnio();
                    continue;
                case 5:
                    mostrarLibrosPorIdioma();
                    continue;
                case 0:
                    System.out.println("Cerrando la aplicacion...");
                    break;
                default:
                    System.out.println("Opcion invalida\n\n");

            }
        }
    }

    private DatosResults getDatosLibros() {
        System.out.println("Escribe el libro que deseas buscar");
        try {
            var nombreLibro = lectura.nextLine().trim();
            if (nombreLibro.isEmpty()){
                throw new RuntimeException("Debe ingresar un titulo");
            }
            var json = consultaAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "%20"));
            DatosResults datosResults = conversor.obtenerDatos(json, DatosResults.class);
            return datosResults;
        }catch (RuntimeException e) {
            System.out.println("Error al buscar el libro: " + e.getMessage()+"\n\n");
        }
        return null; // Retorna null si ocurre algún error o no se encuentran datos
    }

    private void buscarLibrosPorTitulo(){
        // Obtenemos todos los libros segun el titulo
        var datos = getDatosLibros();
        if (datos == null){
            System.out.println("Error, debe ingresar un titulo");
            return;
        }
        List<DatosLibros> datosLibros = getDatosLibros().datosLibros();

        if (datosLibros == null || datosLibros.isEmpty()) {
            System.out.println("No se encontraron libros disponibles.\n\n");
            return;
        }

        // Filtramos los libros segun los autores validos
        List<DatosLibros> libros = datosLibros.stream()
                .filter(l -> l.autor().stream()
                        .findFirst()
                        .map(autor -> autor.fechaNacimiento() !=null)
                        .orElse(false))
                .toList();

        Optional<DatosLibros> libroOptional = libros.stream()
                .sorted(Comparator.comparingInt(DatosLibros::numeroDescargas))
                .findFirst();

        libroOptional.ifPresentOrElse(
                libro -> {
                    // Aquí puedes usar el 'libro' obtenido
                    // Validacion si ya existe el libro antes de guardarlo
                    boolean existeLibro = repositorio.existsByTitulo(libro.titulo());
                    if (!existeLibro) {
                        //Craea un Objeto libro
                        Libro libroNuevo = new Libro(libro);
                        // Crea un record con los datos del autor del libro
                        DatosAutor datosAutor = new DatosAutor(
                                libroNuevo.getAutor().getNombre(),
                                libroNuevo.getAutor().getFechaNacimiento(),
                                libroNuevo.getAutor().getFechaFallecimiento());
                        // Guarda el autor en base de datos.
                        Autor autor = autorRepositorio.save(new Autor(datosAutor));
                        // Guarda el libro en base de datos
                        libroNuevo.setAutor(autor);
                        repositorio.save(libroNuevo);
                        System.out.println("Se ha encontrado y registrado el siguiente libro:  " + libroNuevo.toString()+"\n\n");
                    } else {
                        System.out.println("Se ha encontrado el siguiente registro:  " + libro.toString()+"\n\n");
                    }
                },
                () -> System.out.println("No se encontraron libros disponibles.\n\n")
        );


    }

    private void mostrarLibrosRegistrados(){
        repositorio.findAll().stream().forEach(libro -> System.out.println(libro.toString()));
    }

    private void mostrarAutoresRegistrados(){
        autorRepositorio.findAll().stream().forEach(autor -> System.out.println(autor.toString()));
    }

    public void mostrarAutoresVivosSegunAnio() {
        System.out.println("Escribe el año a buscar");

        try {
            int anio = lectura.nextInt();

            List<Autor> autoresVivos = autorRepositorio.selectAutoresVivos(anio);
            System.out.println(autoresVivos);

            if (!autoresVivos.isEmpty()) {
                System.out.println("Autores vivos en " + anio + ":");
                autoresVivos.forEach(autor -> System.out.println(autor.getNombre()));
            } else {
                System.out.println("No se encontraron autores vivos en el año " + anio+"\n\n");
//                return;
                lectura.nextLine();

            }
        } catch (Exception e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.\n\n");
            lectura.nextLine(); // Limpiar el buffer del Scanner en caso de error
        }
    }

    private void mostrarLibrosPorIdioma() {
            //obtener idioma por input
        System.out.println("Escribe que idioma deseas buscar (en = Ingles, fr = frances, es = español" );
        var idioma = lectura.nextLine().trim().toLowerCase();
            //CREAR VALIDACION DE NOMBRE VALIDO
            //filtrar que muestre solo libros que sean en idioma deseado.
        if (!idioma.equals("en") && !idioma.equals("fr") && !idioma.equals("es")){
            System.out.println("No se a encontrado ese idioma presente en nuestras base de datos.\n\n");
            return;
        }else {
            repositorio.findAll().stream()
                    .filter(i -> i.getIdioma().equals(idioma))
                    .forEach(i -> System.out.println(i.toString()));;
            }


    }
}