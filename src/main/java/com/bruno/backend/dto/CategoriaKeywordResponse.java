package com.bruno.backend.dto;

import com.bruno.backend.entity.CategoriaKeyword;

public record CategoriaKeywordResponse(
        Long id,
        String keyword,
        Long categoriaId,
        String categoriaNome
) {
    public static CategoriaKeywordResponse from(CategoriaKeyword ck) {
        return new CategoriaKeywordResponse(
                ck.getId(),
                ck.getKeyword(),
                ck.getCategoria().getId(),
                ck.getCategoria().getNome()
        );
    }
}

