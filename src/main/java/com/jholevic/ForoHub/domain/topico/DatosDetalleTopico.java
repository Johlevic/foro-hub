package com.jholevic.ForoHub.domain.topico;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public record DatosDetalleTopico(
         Long id,
         String titulo,
         String mensaje,
         LocalDateTime fechaCreacion ,
         String autor,
         Curso curso


) {
    public DatosDetalleTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getAutor(),
                topico.getCurso()
        );
    }

}
