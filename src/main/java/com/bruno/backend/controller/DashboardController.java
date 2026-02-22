package com.bruno.backend.controller;

import com.bruno.backend.dto.DashboardResponse;
import com.bruno.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping
    public DashboardResponse resumoMensal(
            @RequestParam(defaultValue = "0") int ano,
            @RequestParam(defaultValue = "0") int mes) {
        if (ano == 0) ano = LocalDate.now().getYear();
        if (mes == 0) mes = LocalDate.now().getMonthValue();
        log.info("GET /dashboard - entrada: ano={}, mes={}", ano, mes);
        DashboardResponse response = service.resumoMensal(ano, mes);
        log.info("GET /dashboard - saída: receitas={}, despesas={}, saldo={}", response.totalReceitas(), response.totalDespesas(), response.saldo());
        return response;
    }
}
