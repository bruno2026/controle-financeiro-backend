package com.bruno.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        int ano,
        int mes,
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo,
        List<TransacaoResponse> transacoes
) {}

