public class PricingEngine {
    private PricingService pricingService;
    public PricingEngine(PricingService pricingService) {
        this.pricingService = pricingService;
    }
    public PricingResult calculate(
            Product product,
            PricingRule pricingRule,
            ChannelRule channelRule
    ){
        double discount = pricingRule.calculateDiscount();
        if(!pricingRule.isCampaignEligible(discount)){
            throw new InvalidDiscountException(
                    "Discount is not eligible for campaign"
            );
        }
        if(!channelRule.isDiscountAllowed(discount)){
            throw new InvalidDiscountException(
                    "Discount is not allowed for channel"
            );
        }
        double sellingPrice = pricingService.calculateSellingPrice(product, discount);
        double campaignCost = pricingService.calculateCampaignCost(sellingPrice, channelRule);
        double margin = pricingService.calculateMargin(product, sellingPrice, campaignCost);
        boolean marginValid = pricingService.isMarginValid(margin, channelRule);
        return new PricingResult(
                product,
                pricingRule.getChannelName(),
                sellingPrice,
                discount,
                campaignCost,
                margin,
                marginValid
        );
    }
}
