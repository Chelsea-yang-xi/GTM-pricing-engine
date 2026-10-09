package com.gtmpricingengine.model;

public class ChannelRule {
    private String channelName;
    private double defaultDiscount;
    private double maxDiscount;
    private double minimumMargin;
    private double campaignFee;
    private double minimumCampaignDiscount;


    public ChannelRule(
            String channelName,
            double defaultDiscount,
            double maxDiscount,
            double minimumMargin,
            double campaignFee,
            double minimumCampaignDiscount
    ) {
        this.channelName = channelName;
        this.defaultDiscount = defaultDiscount;
        this.maxDiscount =maxDiscount;
        this.minimumMargin =minimumMargin;
        this.campaignFee =campaignFee;
        this.minimumCampaignDiscount =minimumCampaignDiscount;
    }

    public String getChannelName(){
        return channelName;
    }
    public double getDefaultDiscount(){
        return defaultDiscount;
    }
    public double getMaxDiscount(){
        return maxDiscount;
    }
    public double getMinimumMargin(){
        return  minimumMargin;
    }
    public double getCampaignFee(){
        return campaignFee;
    }
    public boolean isDiscountAllowed (double requestedDiscount) {
        return requestedDiscount <= maxDiscount;
    }
    public boolean meetsCampaignDiscountRule(double requestedDiscount) {
    return requestedDiscount >= minimumCampaignDiscount;
    }
}