package com.gtmpricingengine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProductRequest(

        @NotBlank(
                message = "SKU must not be blank"
        )
        String sku,

        @NotBlank(
                message = "Product name must not be blank"
        )
        String name,

        @Positive(
                message = "RRP must be greater than zero"
        )
        double rrp,

        @PositiveOrZero(
                message = "Cost cannot be negative"
        )
        double cost

) {
}