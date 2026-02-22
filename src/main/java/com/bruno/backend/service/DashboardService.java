package com.bruno.backend.service;

import com.bruno.backend.dto.DashboardResponse;
import com.bruno.backend.dto.TransacaoResponse;
import com.bruno.backend.enums.TipoTransacao;
import com.bruno.backend.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransacaoRepository repository;

    public DashboardResponse resumoMensal(int ano, int mes) {
        log.info("Gerando resumo mensal: ano={}, mes={}", ano, mes);
        List<TransacaoResponse> transacoes = repository.findByAnoMes(ano, mes)
                .stream().map(TransacaoResponse::from).toList();

        BigDecimal totalReceitas = repository.sumByTipoAndAnoMes(TipoTransacao.RECEITA, ano, mes);
        BigDecimal totalDespesas = repository.sumByTipoAndAnoMes(TipoTransacao.DESPESA, ano, mes);
        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        log.info("Resumo {}/{}: receitas={}, despesas={}, saldo={}", mes, ano, totalReceitas, totalDespesas, saldo);
        return new DashboardResponse(ano, mes, totalReceitas, totalDespesas, saldo, transacoes);
    }
}
