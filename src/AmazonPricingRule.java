public class AmazonPricingRule implements PricingRule{
    @Override
    public  double calculateDiscount() {
        return 0.15;
    }

    @Override
    public boolean isCampaignEligible(double discount) {
        return discount >= 0.15;
    }

    @Override
    public String getChannelName() {
        return "Amazon";
    }
}
