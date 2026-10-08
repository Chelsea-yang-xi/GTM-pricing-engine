package com.gtmpricingengine.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record PricingRequest(

        @NotBlank(
                message = "SKU must not be blank"
        )
        String sku,

        @NotBlank(
                message = "Channel name must not be blank"
        )
        String channelName,

        @DecimalMin(
                value = "0.0",
                message = "Default discount cannot be negative"
        )
        @DecimalMax(
                value = "1.0",
                message = "Default discount cannot exceed 1"
        )
        double defaultDiscount,

        @DecimalMin(
                value = "0.0"
        )
        @DecimalMax(
                value = "1.0"
        )
        double maxDiscount,

        @DecimalMin(
                value = "0.0"
        )
        @DecimalMax(
                value = "1.0"
        )
        double minimumMargin,

        @DecimalMin(
                value = "0.0"
        )
        @DecimalMax(
                value = "1.0"
        )
        double campaignFee,

        @DecimalMin(
                value = "0.0"
        )
        @DecimalMax(
                value = "1.0"
        )
        double minimumCampaignDiscount

) {
}