package com.bruno.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaKeywordRequest(
        @NotBlank @Size(max = 100) String keyword,
        @NotNull Long categoriaId
) {}

