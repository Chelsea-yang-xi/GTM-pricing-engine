import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        // ==============================
        // 1. Create Product Catalog
        // ==============================

        ProductCatalog catalog = new ProductCatalog();

        Product product1 =
                new Product(
                        "A2148",
                        "Anker PowerCore",
                        59.99,
                        30
                );

        Product product2 =
                new Product(
                        "T8410",
                        "Eufy Indoor Cam",
                        99.99,
                        45
                );

        Product product3 =
                new Product(
                        "Q30",
                        "Soundcore Q30",
                        79.99,
                        38
                );

        catalog.addProduct(product1);
        catalog.addProduct(product2);
        catalog.addProduct(product3);


        // ==============================
        // 2. Test Product Catalog
        // ==============================

        Product selected =
                catalog.getProduct("A2148");

        System.out.println(
                selected.getName()
        );

        System.out.println(
                "RRP: " + selected.getRrp()
        );

        System.out.println(
                "Products in catalog: "
                        + catalog.size()
        );


        // ==============================
        // 3. Channel Rules
        // ==============================

        Map<String, ChannelRule> channelRules =
                new HashMap<>();

        channelRules.put(
                "Amazon",
                new ChannelRule(
                        "Amazon",
                        0.15,
                        0.30,
                        0.35,
                        0.03,
                        0.15
                )
        );

        channelRules.put(
                "Coolblue",
                new ChannelRule(
                        "Coolblue",
                        0.20,
                        0.30,
                        0.40,
                        0.02,
                        0.10
                )
        );

        channelRules.put(
                "Bol.com",
                new ChannelRule(
                        "Bol.com",
                        0.10,
                        0.20,
                        0.35,
                        0.02,
                        0.05
                )
        );


        // ==============================
        // 4. Pricing Strategies
        // ==============================

        List<PricingRule> pricingRules =
                new ArrayList<>();

        pricingRules.add(
                new AmazonPricingRule()
        );

        pricingRules.add(
                new BolPricingRule()
        );

        pricingRules.add(
                new CoolbluePricingRule()
        );


        // ==============================
        // 5. Pricing Service
        // ==============================

        PricingService pricingService =
                new PricingService();
        PricingEngine engine =
                new PricingEngine(pricingService);


        // ==============================
        // 6. Run Pricing Engine
        // ==============================

        for (PricingRule pricingRule : pricingRules) {

            String channel =
                    pricingRule.getChannelName();

            ChannelRule channelRule =
                    channelRules.get(channel);
            if (channelRule == null) {
                System.out.println(
                        "No channel rule found for "
                                + channel
                );
                continue;
            }

            System.out.println();

            System.out.println(
                    "========== "
                            + channel
                            + " =========="
            );


            for (Product product :
                    catalog.getAllProducts()) {

                try {
                    PricingResult result = engine.calculate(
                        product,
                        pricingRule,
                        channelRule
                    );

                        System.out.println(
                                result
                                        .getProduct()
                                        .getName()
                                        + " | Price: "
                                        + result.getSellingPrice()
                                        + " | Discount: "
                                        + result.getDiscount()
                                        + " | Campaign Cost: "
                                        + result.getCampaignCost()
                                        + " | Margin: "
                                        + result.getMargin()
                        );

                    if (result.isMarginValid()) {

                        System.out.println(
                                "Margin OK"
                        );

                    } else {

                        System.out.println(
                                "WARNING: Margin too low"
                                + channel
                                + "minimum margin: "
                                + channelRule.getMinimumMargin()
                        );
                    }


                } catch (IllegalArgumentException e){

                    System.out.println(
                            product.getName()
                                    + " | Rejected: "
                                    + e.getMessage()
                    );
                } catch (InvalidDiscountException e) {

                    System.out.println(
                            product.getName()
                                    + " | Rejected: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }
}