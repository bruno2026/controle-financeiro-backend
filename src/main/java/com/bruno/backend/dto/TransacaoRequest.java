package com.bruno.backend.dto;

import com.bruno.backend.enums.StatusTransacao;
import com.bruno.backend.enums.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequest(
        @NotBlank String descricao,
        @NotNull @Positive BigDecimal valor,
        @NotNull TipoTransacao tipo,
        StatusTransacao status,
        @NotNull LocalDate data,
        Long categoriaId
) {}

