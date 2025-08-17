package com.jholevic.ForoHub.domain.topico;

import java.time.LocalDateTime;

public record DatosListaTopico(
        Long id,
        String titulo,
        String autor,
        LocalDateTime fechaCreacion

) {
    public DatosListaTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getAutor(),
                topico.getFechaCreacion()

        );
    }
}
