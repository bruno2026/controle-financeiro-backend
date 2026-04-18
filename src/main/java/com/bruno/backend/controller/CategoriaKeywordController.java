package com.bruno.backend.controller;

import com.bruno.backend.dto.CategoriaKeywordRequest;
import com.bruno.backend.dto.CategoriaKeywordResponse;
import com.bruno.backend.service.CategoriaKeywordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categoria-keywords")
@RequiredArgsConstructor
public class CategoriaKeywordController {

    private final CategoriaKeywordService service;

    @GetMapping
    public List<CategoriaKeywordResponse> listar() {
        log.info("GET /categoria-keywords - entrada");
        List<CategoriaKeywordResponse> response = service.listar();
        log.info("GET /categoria-keywords - saída: {} registros", response.size());
        return response;
    }

    @PostMapping
    public ResponseEntity<CategoriaKeywordResponse> criar(@Valid @RequestBody CategoriaKeywordRequest request) {
        log.info("POST /categoria-keywords - entrada: keyword={}, categoriaId={}", request.keyword(), request.categoriaId());
        CategoriaKeywordResponse response = service.criar(request);
        log.info("POST /categoria-keywords - saída: id={}, keyword={}", response.id(), response.keyword());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("DELETE /categoria-keywords/{} - entrada", id);
        service.deletar(id);
        log.info("DELETE /categoria-keywords/{} - saída: deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}

