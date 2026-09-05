public class PricingResult {

    private Product product;
    private String channel;
    private double sellingPrice;
    private double discount;
    private double campaignCost;
    private double margin;
    private boolean marginValid;

    public PricingResult(
            Product product,
            String channel,
            double sellingPrice,
            double discount,
            double campaignCost,
            double margin,
            boolean marginValid
    ) {
        this.product = product;
        this.channel = channel;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.campaignCost = campaignCost;
        this.margin = margin;
        this.marginValid = marginValid;
    }
    public Product getProduct() {
        return product;
    }
    public String getChannel() {
        return channel;
    }
    public double getSellingPrice() {
        return sellingPrice;
    }
    public double getDiscount() {
        return discount;
    }
    public double getCampaignCost() {
        return campaignCost;
    }
    public double getMargin() {
        return margin;
    }
    public boolean isMarginValid() {
        return marginValid;
    }
}
