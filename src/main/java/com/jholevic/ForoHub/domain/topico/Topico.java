package com.jholevic.ForoHub.domain.topico;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

@Table(name = "topicos")
@Entity(name = "Topico")

public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean activo; // 👈 este campo faltaba
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion ;

    private String autor;

    @Enumerated(EnumType.STRING)
    private Curso curso;


    public Topico(@Valid DatosRegistroTopico datos) {
        this.titulo = datos.titulo();
        this.activo = true;
        this.mensaje = datos.mensaje();
        this.autor = datos.autor();
        this.fechaCreacion = LocalDateTime.now();
        this.curso = datos.curso();
    }

    public void eliminar() {
        this.activo = false;
    }

    public void actualizarInformaciones(@Valid DatosActualizacionTopico datos) {

        if (datos.titulo() != null) {
            this.titulo = datos.titulo();
        }
        if (datos.mensaje() != null) {
            this.mensaje = datos.mensaje();
        }
        if (datos.curso() != null) {
            this.curso =datos.curso();
        }

    }
}
