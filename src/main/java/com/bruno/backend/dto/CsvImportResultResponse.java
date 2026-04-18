package com.bruno.backend.dto;

import java.util.List;

public record CsvImportResultResponse(
        int importadas,
        int ignoradas,
        List<String> detalhesIgnoradas
) {}

