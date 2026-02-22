package com.bruno.backend.service;

import com.bruno.backend.dto.CategoriaRequest;
import com.bruno.backend.dto.CategoriaResponse;
import com.bruno.backend.entity.Categoria;
import com.bruno.backend.exception.BusinessException;
import com.bruno.backend.exception.ResourceNotFoundException;
import com.bruno.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    public List<CategoriaResponse> listar() {
        log.info("Listando todas as categorias");
        return repository.findAll().stream().map(CategoriaResponse::from).toList();
    }

    public CategoriaResponse buscarPorId(Long id) {
        log.info("Buscando categoria id: {}", id);
        return CategoriaResponse.from(findOrThrow(id));
    }

    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        log.info("Criando categoria: nome={}", request.nome());
        if (repository.existsByNomeIgnoreCase(request.nome())) {
            log.warn("Categoria com nome '{}' já existe", request.nome());
            throw new BusinessException("Já existe uma categoria com o nome: " + request.nome());
        }
        Categoria categoria = Categoria.builder()
                .nome(request.nome())
                .build();
        CategoriaResponse response = CategoriaResponse.from(repository.save(categoria));
        log.info("Categoria criada com id: {}", response.id());
        return response;
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        log.info("Atualizando categoria id: {}", id);
        Categoria categoria = findOrThrow(id);
        categoria.setNome(request.nome());
        CategoriaResponse response = CategoriaResponse.from(repository.save(categoria));
        log.info("Categoria id: {} atualizada com sucesso", id);
        return response;
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando categoria id: {}", id);
        findOrThrow(id);
        repository.deleteById(id);
        log.info("Categoria id: {} deletada com sucesso", id);
    }

    public Categoria findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Categoria não encontrada: id={}", id);
                    return new ResourceNotFoundException("Categoria não encontrada: " + id);
                });
    }
}
