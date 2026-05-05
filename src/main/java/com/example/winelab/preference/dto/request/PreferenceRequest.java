package com.example.winelab.preference.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class PreferenceRequest {

    private Long userId;

    @NotNull
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private Double tannin;

    @NotNull
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private Double acidity;

    @NotNull
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private Double body;

    @NotNull
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    private Double sweetness;
}