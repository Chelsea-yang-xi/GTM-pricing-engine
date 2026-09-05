public class PricingService {
    public double calculateSellingPrice(
            Product product,
            double discount
    ) {
        if(discount <0){
            throw new InvalidDiscountException(
                    "Discount cannot be negative"
            );
        }

        if(discount >1){
            throw new InvalidDiscountException(
                    "Discount cannot be greater than 1"
            );
        }
        return product.getRrp()*(1-discount);
    }
    public double calculateCampaignCost(double sellingPrice, ChannelRule rule) {
        return sellingPrice* rule.getCampaignFee();
    }
    public double calculateMargin(
            Product product,
            double sellingPrice,
            double campaignCost
    ) {
        if (sellingPrice <= 0) {
            throw new InvalidDiscountException("Selling price must be greater than zero");
        }
        return (
                sellingPrice
                        - product.getCost()
                        - campaignCost
        )/ sellingPrice;
    }
    public boolean isMarginValid(double margin, ChannelRule rule) {
        return margin >= rule.getMinimumMargin();
    }

}
