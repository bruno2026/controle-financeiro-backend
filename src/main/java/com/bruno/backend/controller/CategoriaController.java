package com.bruno.backend.controller;

import com.bruno.backend.dto.CategoriaRequest;
import com.bruno.backend.dto.CategoriaResponse;
import com.bruno.backend.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @GetMapping
    public List<CategoriaResponse> listar() {
        log.info("GET /categorias - entrada");
        List<CategoriaResponse> response = service.listar();
        log.info("GET /categorias - saída: retornando {} registros", response.size());
        return response;
    }

    @GetMapping("/{id}")
    public CategoriaResponse buscarPorId(@PathVariable Long id) {
        log.info("GET /categorias/{} - entrada", id);
        CategoriaResponse response = service.buscarPorId(id);
        log.info("GET /categorias/{} - saída: nome={}", id, response.nome());
        return response;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(@Valid @RequestBody CategoriaRequest request) {
        log.info("POST /categorias - entrada: nome={}", request.nome());
        CategoriaResponse response = service.criar(request);
        log.info("POST /categorias - saída: id={}, nome={}", response.id(), response.nome());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public CategoriaResponse atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
        log.info("PUT /categorias/{} - entrada: nome={}", id, request.nome());
        CategoriaResponse response = service.atualizar(id, request);
        log.info("PUT /categorias/{} - saída: nome={}", id, response.nome());
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("DELETE /categorias/{} - entrada", id);
        service.deletar(id);
        log.info("DELETE /categorias/{} - saída: deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}
