package com.gtmpricingengine.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PricingRequest(

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

        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "Cost cannot be negative"
        )
        double cost,

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
                value = "0.0",
                message = "Maximum discount cannot be negative"
        )
        @DecimalMax(
                value = "1.0",
                message = "Maximum discount cannot exceed 1"
        )
        double maxDiscount,

        @DecimalMin(
                value = "0.0",
                message = "Minimum margin cannot be negative"
        )
        @DecimalMax(
                value = "1.0",
                message = "Minimum margin cannot exceed 1"
        )
        double minimumMargin,

        @DecimalMin(
                value = "0.0",
                message = "Campaign fee cannot be negative"
        )
        @DecimalMax(
                value = "1.0",
                message = "Campaign fee cannot exceed 1"
        )
        double campaignFee,

        @DecimalMin(
                value = "0.0",
                message = "Minimum campaign discount cannot be negative"
        )
        @DecimalMax(
                value = "1.0",
                message = "Minimum campaign discount cannot exceed 1"
        )
        double minimumCampaignDiscount

) {
}