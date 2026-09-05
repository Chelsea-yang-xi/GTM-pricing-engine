package com.gtmpricingengine.rule;

public class CoolbluePricingRule implements PricingRule {
    @Override
    public double calculateDiscount() {
        return 0.20;
    }

    @Override
    public boolean isCampaignEligible(double discount) {
        return discount >= 0.10;
    }

    @Override
    public String getChannelName() {
        return "Coolblue";
    }
}
