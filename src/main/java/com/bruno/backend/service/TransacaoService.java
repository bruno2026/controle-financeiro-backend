package com.bruno.backend.service;

import com.bruno.backend.dto.TransacaoRequest;
import com.bruno.backend.dto.TransacaoResponse;
import com.bruno.backend.entity.Categoria;
import com.bruno.backend.entity.Transacao;
import com.bruno.backend.enums.StatusTransacao;
import com.bruno.backend.enums.TipoTransacao;
import com.bruno.backend.exception.ResourceNotFoundException;
import com.bruno.backend.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository repository;
    private final CategoriaService categoriaService;

    public List<TransacaoResponse> listar() {
        log.info("Listando todas as transações");
        return repository.findAll().stream().map(TransacaoResponse::from).toList();
    }

    public List<TransacaoResponse> listarPorTipo(TipoTransacao tipo) {
        log.info("Listando transações por tipo: {}", tipo);
        return repository.findByTipo(tipo).stream().map(TransacaoResponse::from).toList();
    }

    public TransacaoResponse buscarPorId(Long id) {
        log.info("Buscando transação id: {}", id);
        return TransacaoResponse.from(findOrThrow(id));
    }

    @Transactional
    public TransacaoResponse criar(TransacaoRequest request) {
        log.info("Criando transação: tipo={}, valor={}, data={}", request.tipo(), request.valor(), request.data());
        Transacao transacao = buildTransacao(new Transacao(), request);
        repository.save(transacao);
        repository.flush();
        TransacaoResponse response = TransacaoResponse.from(findOrThrow(transacao.getId()));
        log.info("Transação criada com id: {}", response.id());
        return response;
    }

    @Transactional
    public TransacaoResponse atualizar(Long id, TransacaoRequest request) {
        log.info("Atualizando transação id: {}", id);
        Transacao transacao = findOrThrow(id);
        buildTransacao(transacao, request);
        repository.save(transacao);
        repository.flush();
        TransacaoResponse response = TransacaoResponse.from(findOrThrow(id));
        log.info("Transação id: {} atualizada com sucesso", id);
        return response;
    }

    @Transactional
    public TransacaoResponse atualizarCategoria(Long id, Long categoriaId) {
        log.info("Atualizando categoria da transação id: {} para categoriaId: {}", id, categoriaId);
        Transacao transacao = findOrThrow(id);
        Categoria categoria = categoriaId != null ? categoriaService.findOrThrow(categoriaId) : null;
        transacao.setCategoria(categoria);
        repository.save(transacao);
        repository.flush();
        TransacaoResponse response = TransacaoResponse.from(findOrThrow(id));
        log.info("Categoria da transação id: {} atualizada com sucesso", id);
        return response;
    }

    @Transactional
    public TransacaoResponse atualizarStatus(Long id, StatusTransacao status) {
        log.info("Atualizando status da transação id: {} para {}", id, status);
        Transacao transacao = findOrThrow(id);
        transacao.setStatus(status);
        repository.save(transacao);
        repository.flush();
        TransacaoResponse response = TransacaoResponse.from(findOrThrow(id));
        log.info("Status da transação id: {} atualizado para {}", id, status);
        return response;
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando transação id: {}", id);
        findOrThrow(id);
        repository.deleteById(id);
        log.info("Transação id: {} deletada com sucesso", id);
    }

    public Transacao findOrThrow(Long id) {
        return repository.findByIdWithCategoria(id)
                .orElseThrow(() -> {
                    log.warn("Transação não encontrada: id={}", id);
                    return new ResourceNotFoundException("Transação não encontrada: " + id);
                });
    }

    private Transacao buildTransacao(Transacao transacao, TransacaoRequest request) {
        Categoria categoria = request.categoriaId() != null
                ? categoriaService.findOrThrow(request.categoriaId())
                : null;
        transacao.setDescricao(request.descricao());
        transacao.setValor(request.valor());
        transacao.setTipo(request.tipo());
        transacao.setStatus(request.status() != null ? request.status() : StatusTransacao.PENDENTE);
        transacao.setData(request.data());
        transacao.setCategoria(categoria);
        return transacao;
    }
}
