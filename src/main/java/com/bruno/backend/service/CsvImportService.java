package com.bruno.backend.service;

import com.bruno.backend.dto.CsvImportResultResponse;
import com.bruno.backend.entity.Categoria;
import com.bruno.backend.entity.CategoriaKeyword;
import com.bruno.backend.entity.Transacao;
import com.bruno.backend.enums.StatusTransacao;
import com.bruno.backend.enums.TipoTransacao;
import com.bruno.backend.repository.CategoriaKeywordRepository;
import com.bruno.backend.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvImportService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaKeywordRepository categoriaKeywordRepository;

    @Transactional
    public CsvImportResultResponse importar(MultipartFile file) {
        log.info("Iniciando importação de CSV: arquivo={}, tamanho={} bytes", file.getOriginalFilename(), file.getSize());

        List<CategoriaKeyword> keywords = categoriaKeywordRepository.findAllWithCategoria();
        log.info("Keywords de categorias carregadas: {} registros", keywords.size());

        int importadas = 0;
        int ignoradas = 0;
        List<String> detalhesIgnoradas = new ArrayList<>();

        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVParser parser = CSVFormat.DEFAULT.builder()
                    .setHeader("date", "title", "amount")
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .build()
                    .parse(reader)
        ) {
            for (CSVRecord record : parser) {
                String dateStr = record.get("date");
                String title = record.get("title");
                String amountStr = record.get("amount");

                log.debug("Processando linha {}: date={}, title={}, amount={}", record.getRecordNumber(), dateStr, title, amountStr);

                BigDecimal amount;
                try {
                    amount = new BigDecimal(amountStr);
                } catch (NumberFormatException e) {
                    String motivo = "Linha " + record.getRecordNumber() + " ignorada: valor inválido '" + amountStr + "' (title=" + title + ")";
                    log.warn(motivo);
                    detalhesIgnoradas.add(motivo);
                    ignoradas++;
                    continue;
                }

                // Valores negativos são créditos/estornos — ignorar
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    String motivo = "Linha " + record.getRecordNumber() + " ignorada: valor não positivo " + amount + " (title=" + title + ")";
                    log.info(motivo);
                    detalhesIgnoradas.add(motivo);
                    ignoradas++;
                    continue;
                }

                LocalDate data;
                try {
                    data = LocalDate.parse(dateStr);
                } catch (Exception e) {
                    String motivo = "Linha " + record.getRecordNumber() + " ignorada: data inválida '" + dateStr + "' (title=" + title + ")";
                    log.warn(motivo);
                    detalhesIgnoradas.add(motivo);
                    ignoradas++;
                    continue;
                }

                Categoria categoria = classificarCategoria(title, keywords);
                if (categoria != null) {
                    log.debug("Categoria identificada para '{}': {}", title, categoria.getNome());
                } else {
                    log.debug("Nenhuma categoria identificada para '{}'", title);
                }

                Transacao transacao = Transacao.builder()
                        .descricao(title)
                        .valor(amount)
                        .tipo(TipoTransacao.DESPESA)
                        .status(StatusTransacao.PENDENTE)
                        .data(data)
                        .categoria(categoria)
                        .build();

                transacaoRepository.save(transacao);
                importadas++;
                log.debug("Transação criada: descricao='{}', valor={}, data={}, categoria={}", title, amount, data, categoria != null ? categoria.getNome() : "sem categoria");
            }
        } catch (Exception e) {
            log.error("Erro ao processar CSV: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao processar o arquivo CSV: " + e.getMessage(), e);
        }

        log.info("Importação concluída: importadas={}, ignoradas={}", importadas, ignoradas);
        return new CsvImportResultResponse(importadas, ignoradas, detalhesIgnoradas);
    }

    /**
     * Verifica se alguma keyword cadastrada está contida no título (case-insensitive).
     * Retorna a categoria da primeira keyword que der match.
     */
    private Categoria classificarCategoria(String title, List<CategoriaKeyword> keywords) {
        String titleLower = title.toLowerCase();
        for (CategoriaKeyword ck : keywords) {
            if (titleLower.contains(ck.getKeyword().toLowerCase())) {
                return ck.getCategoria();
            }
        }
        return null;
    }
}

