package com.gtmpricingengine.dto;

public record PricingRequest(

        String sku,

        String name,

        double rrp,

        double cost,

        String channelName,

        double defaultDiscount,

        double maxDiscount,

        double minimumMargin,

        double campaignFee,

        double minimumCampaignDiscount

) {
}