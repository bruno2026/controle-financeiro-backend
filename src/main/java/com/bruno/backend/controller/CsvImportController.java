package com.bruno.backend.controller;

import com.bruno.backend.dto.CsvImportResultResponse;
import com.bruno.backend.service.CsvImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/importacao")
@RequiredArgsConstructor
public class CsvImportController {

    private final CsvImportService service;

    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CsvImportResultResponse> importarCsv(@RequestParam("file") MultipartFile file) {
        log.info("POST /importacao/csv - entrada: arquivo={}, tamanho={} bytes", file.getOriginalFilename(), file.getSize());
        CsvImportResultResponse response = service.importar(file);
        log.info("POST /importacao/csv - saída: importadas={}, ignoradas={}", response.importadas(), response.ignoradas());
        return ResponseEntity.ok(response);
    }
}

