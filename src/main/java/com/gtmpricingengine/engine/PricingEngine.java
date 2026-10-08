package com.gtmpricingengine.engine;

import com.gtmpricingengine.dto.PricingResult;
import com.gtmpricingengine.model.ChannelRule;
import com.gtmpricingengine.service.PricingService;
import com.gtmpricingengine.model.Product;
import com.gtmpricingengine.exception.InvalidDiscountException;

public class PricingEngine {

    private final PricingService pricingService;

    public PricingEngine(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    public PricingResult calculate(
            Product product,
            ChannelRule channelRule
    ) {

        double discount =
                channelRule.getDefaultDiscount();

        if (!channelRule.meetsCampaignDiscountRule(discount)) {
            throw new InvalidDiscountException(
                    "Discount is not eligible for campaign"
            );
        }

        if (!channelRule.isDiscountAllowed(discount)) {
            throw new InvalidDiscountException(
                    "Discount is not allowed for channel"
            );
        }

        double sellingPrice =
                pricingService.calculateSellingPrice(
                        product,
                        discount
                );

        double campaignCost =
                pricingService.calculateCampaignCost(
                        sellingPrice,
                        channelRule
                );

        double margin =
                pricingService.calculateMargin(
                        product,
                        sellingPrice,
                        campaignCost
                );

        boolean marginValid =
                pricingService.isMarginValid(
                        margin,
                        channelRule
                );

        return new PricingResult(
                product,
                channelRule.getChannelName(),
                sellingPrice,
                discount,
                campaignCost,
                margin,
                marginValid
        );
    }
}