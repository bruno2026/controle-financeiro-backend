package com.bruno.backend.dto;

import com.bruno.backend.entity.Transacao;
import com.bruno.backend.enums.StatusTransacao;
import com.bruno.backend.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        TipoTransacao tipo,
        StatusTransacao status,
        LocalDate data,
        CategoriaResponse categoria
) {
    public static TransacaoResponse from(Transacao t) {
        return new TransacaoResponse(
                t.getId(),
                t.getDescricao(),
                t.getValor(),
                t.getTipo(),
                t.getStatus(),
                t.getData(),
                t.getCategoria() != null ? CategoriaResponse.from(t.getCategoria()) : null
        );
    }
}

