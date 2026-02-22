package com.bruno.backend.dto;

import com.bruno.backend.entity.Categoria;

public record CategoriaResponse(
        Long id,
        String nome
) {
    public static CategoriaResponse from(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNome());
    }
}
