package com.bruno.backend.controller;

import com.bruno.backend.dto.AtualizarCategoriaRequest;
import com.bruno.backend.dto.TransacaoRequest;
import com.bruno.backend.dto.TransacaoResponse;
import com.bruno.backend.enums.StatusTransacao;
import com.bruno.backend.enums.TipoTransacao;
import com.bruno.backend.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService service;

    @GetMapping
    public List<TransacaoResponse> listar(@RequestParam(required = false) TipoTransacao tipo) {
        log.info("GET /transacoes - tipo={}", tipo);
        List<TransacaoResponse> response = tipo != null ? service.listarPorTipo(tipo) : service.listar();
        log.info("GET /transacoes - retornando {} registros", response.size());
        return response;
    }

    @GetMapping("/{id}")
    public TransacaoResponse buscarPorId(@PathVariable Long id) {
        log.info("GET /transacoes/{}", id);
        TransacaoResponse response = service.buscarPorId(id);
        log.info("GET /transacoes/{} - retornado: tipo={}, valor={}, status={}", id, response.tipo(), response.valor(), response.status());
        return response;
    }

    @PostMapping
    public ResponseEntity<TransacaoResponse> criar(@Valid @RequestBody TransacaoRequest request) {
        log.info("POST /transacoes - entrada: tipo={}, valor={}, data={}, categoriaId={}", request.tipo(), request.valor(), request.data(), request.categoriaId());
        TransacaoResponse response = service.criar(request);
        log.info("POST /transacoes - saída: id={}, tipo={}, valor={}, status={}", response.id(), response.tipo(), response.valor(), response.status());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public TransacaoResponse atualizar(@PathVariable Long id, @Valid @RequestBody TransacaoRequest request) {
        log.info("PUT /transacoes/{} - entrada: tipo={}, valor={}, data={}", id, request.tipo(), request.valor(), request.data());
        TransacaoResponse response = service.atualizar(id, request);
        log.info("PUT /transacoes/{} - atualizado: tipo={}, valor={}, status={}", id, response.tipo(), response.valor(), response.status());
        return response;
    }

    @PatchMapping("/{id}/categoria")
    public TransacaoResponse atualizarCategoria(@PathVariable Long id, @RequestBody AtualizarCategoriaRequest request) {
        log.info("PATCH /transacoes/{}/categoria - entrada: categoriaId={}", id, request.categoriaId());
        TransacaoResponse response = service.atualizarCategoria(id, request.categoriaId());
        log.info("PATCH /transacoes/{}/categoria - saída: categoria={}", id, response.categoria());
        return response;
    }

    @PatchMapping("/{id}/status")
    public TransacaoResponse atualizarStatus(@PathVariable Long id, @RequestParam StatusTransacao status) {
        log.info("PATCH /transacoes/{}/status - entrada: status={}", id, status);
        TransacaoResponse response = service.atualizarStatus(id, status);
        log.info("PATCH /transacoes/{}/status - saída: status={}", id, response.status());
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("DELETE /transacoes/{}", id);
        service.deletar(id);
        log.info("DELETE /transacoes/{} - saída: deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}
