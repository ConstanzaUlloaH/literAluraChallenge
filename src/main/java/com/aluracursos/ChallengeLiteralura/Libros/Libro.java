package com.aluracursos.ChallengeLiteralura.Libros;


import jakarta.persistence.*;


@Entity
@Table(name = "datosLibro")
public class Libro {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    //    @OneToMany()
//    private List<DatosAutor> autor;
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;
    private String idioma;
    private Integer cantidadDescargas;


    public Libro() {
    }

    public Libro(DatosLibros datos) {
        if (datos.autor() != null && !datos.autor().isEmpty()) {
            this.autor = new Autor(datos.autor().get(0)); // Considera solo el primer autor
        }
        this.titulo = datos.titulo();
        this.idioma = String.join(", ", datos.idioma()); // ["en", "fr"] --> "en, fr"

        this.cantidadDescargas = datos.numeroDescargas();
    }



    @Override
    public String toString() {
        return   "Titulo = " + titulo + ", "+
                autor +
                ", Idioma = " + idioma +
                ", Cantidad de descargas = " + cantidadDescargas ;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getCantidadDescargas() {
        return cantidadDescargas;
    }

    public void setCantidadDescargas(Integer cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }

}