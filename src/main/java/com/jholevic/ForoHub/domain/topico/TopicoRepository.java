package com.jholevic.ForoHub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository <Topico, Long> {
    Page<Topico> findAllByActivoTrue(Pageable paginacion);

    boolean existsByTituloAndMensaje(@NotBlank String titulo, @NotBlank String mensaje);

    Page<Topico> findByCurso(Curso curso, Pageable paginacion);

    @Query("SELECT t FROM Topico t WHERE YEAR(t.fechaCreacion) = :anio")
    Page<Topico> findByFechaCreacionYear(@Param("anio") Integer anio, Pageable paginacion);

    @Query("SELECT t FROM Topico t WHERE t.curso = :curso AND YEAR(t.fechaCreacion) = :anio")
    Page<Topico> findByCursoAndFechaCreacionYear(@Param("curso") Curso curso, @Param("anio") Integer anio, Pageable paginacion);
}
