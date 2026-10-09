package com.gtmpricingengine.rule;

public class BolPricingRule implements PricingRule {
    @Override
    public double calculateDiscount() {
        return 0.10;
    }

    @Override
    public boolean isCampaignEligible(double discount) {
        return discount >= 0.5;
    }

    @Override
    public String getChannelName() {
        return "Bol.com";
    }
}
