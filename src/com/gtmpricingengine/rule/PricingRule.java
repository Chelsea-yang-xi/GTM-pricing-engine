package com.gtmpricingengine.rule;

public interface PricingRule {
    double calculateDiscount();
    boolean isCampaignEligible(double discount);
    String getChannelName();
}
