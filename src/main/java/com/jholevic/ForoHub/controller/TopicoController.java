package com.jholevic.ForoHub.controller;

import com.jholevic.ForoHub.domain.topico.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")

public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Transactional
    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroTopico datos,
                                    UriComponentsBuilder uriComponentsBuilder) {

        // Verificar si ya existe un tópico con el mismo título y mensaje
        boolean existe = repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje());
        if (existe) {
            return ResponseEntity
                    .badRequest()
                    .body("Ya existe un tópico con el mismo título y mensaje.");
        }

        // Crear y guardar el tópico
        var topico = new Topico(datos);
        repository.save(topico);

        // Construir la URI del nuevo recurso
        var uri = uriComponentsBuilder
                .path("/topicos/{id}")
                .buildAndExpand(topico.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleTopico(topico));
    }


    @GetMapping
    public ResponseEntity<Page<DatosListaTopico>> listar(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion,
            @RequestParam(required = false) Curso curso,
            @RequestParam(required = false) Integer anio
    ) {
        Page<Topico> page;

        if (curso != null && anio != null) {
            // Filtrar por curso y año
            page = repository.findByCursoAndFechaCreacionYear(curso, anio, paginacion);
        } else if (curso != null) {
            page = repository.findByCurso(curso, paginacion);
        } else if (anio != null) {
            page = repository.findByFechaCreacionYear(anio, paginacion);
        } else {
            // Todos los tópicos activos, paginados y ordenados por fecha
            page = repository.findAllByActivoTrue(paginacion);
        }

        return ResponseEntity.ok(page.map(DatosListaTopico::new));
    }



    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizacionTopico datos) {
        var optionalTopico = repository.findById(id);

        if (optionalTopico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var topico = optionalTopico.get();

        // Validar reglas de negocio: evitar duplicados
        boolean existeDuplicado = repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje());
        if (existeDuplicado &&
                (!topico.getTitulo().equals(datos.titulo()) || !topico.getMensaje().equals(datos.mensaje()))) {
            return ResponseEntity.badRequest()
                    .body("Ya existe un tópico con el mismo título y mensaje.");
        }

        topico.actualizarInformaciones(datos);
        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }



    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id) {
        var topico = repository.getReferenceById(id);
        topico.eliminar();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detallar(@PathVariable Long id) {
        var topico = repository.getReferenceById(id);

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

}
