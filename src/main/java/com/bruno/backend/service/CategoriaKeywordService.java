package com.bruno.backend.service;

import com.bruno.backend.dto.CategoriaKeywordRequest;
import com.bruno.backend.dto.CategoriaKeywordResponse;
import com.bruno.backend.entity.Categoria;
import com.bruno.backend.entity.CategoriaKeyword;
import com.bruno.backend.exception.ResourceNotFoundException;
import com.bruno.backend.repository.CategoriaKeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaKeywordService {

    private final CategoriaKeywordRepository repository;
    private final CategoriaService categoriaService;

    public List<CategoriaKeywordResponse> listar() {
        log.info("Listando todas as keywords");
        return repository.findAllWithCategoria().stream().map(CategoriaKeywordResponse::from).toList();
    }

    @Transactional
    public CategoriaKeywordResponse criar(CategoriaKeywordRequest request) {
        log.info("Criando keyword: keyword={}, categoriaId={}", request.keyword(), request.categoriaId());
        Categoria categoria = categoriaService.findOrThrow(request.categoriaId());
        CategoriaKeyword ck = CategoriaKeyword.builder()
                .keyword(request.keyword().toLowerCase())
                .categoria(categoria)
                .build();
        CategoriaKeywordResponse response = CategoriaKeywordResponse.from(repository.save(ck));
        log.info("Keyword criada: id={}, keyword={}, categoria={}", response.id(), response.keyword(), response.categoriaNome());
        return response;
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando keyword id: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Keyword não encontrada: " + id);
        }
        repository.deleteById(id);
        log.info("Keyword id: {} deletada com sucesso", id);
    }
}

