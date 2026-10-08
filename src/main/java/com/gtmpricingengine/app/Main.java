package com.gtmpricingengine.app;

import java.util.LinkedHashMap;
import java.util.Map;

import com.gtmpricingengine.dto.PricingResult;
import com.gtmpricingengine.engine.PricingEngine;
import com.gtmpricingengine.exception.InvalidDiscountException;
import com.gtmpricingengine.model.ChannelRule;
import com.gtmpricingengine.model.Product;
import com.gtmpricingengine.repository.InMemoryProductRepository;
import com.gtmpricingengine.repository.ProductRepository;
import com.gtmpricingengine.service.PricingService;

public class Main {

    public static void main(String[] args) {

        // ==============================
        // 1. Create Product Repository
        // ==============================

        ProductRepository productRepository =
                new InMemoryProductRepository();

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

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);


        // ==============================
        // 2. Test Product Repository
        // ==============================

        Product selected =
                productRepository.findBySku("A2148");

        System.out.println(
                selected.getName()
        );

        System.out.println(
                "RRP: " + selected.getRrp()
        );

        System.out.println(
                "Products in catalog: "
                        + productRepository.count()
        );


        // ==============================
        // 3. Channel Rules
        // ==============================

        Map<String, ChannelRule> channelRules =
                new LinkedHashMap<>();

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
        // 4. Pricing Engine
        // ==============================

        PricingService pricingService =
                new PricingService();

        PricingEngine engine =
                new PricingEngine(pricingService);


        // ==============================
        // 5. Run Pricing Engine
        // ==============================

        for (ChannelRule channelRule :
                channelRules.values()) {

            String channel =
                    channelRule.getChannelName();

            System.out.println();

            System.out.println(
                    "========== "
                            + channel
                            + " =========="
            );


            for (Product product :
                    productRepository.findAll()) {

                try {

                    PricingResult result =
                            engine.calculate(
                                    product,
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
                                        + " | Channel: "
                                        + channel
                                        + " | Minimum margin: "
                                        + channelRule.getMinimumMargin()
                        );
                    }

                } catch (InvalidDiscountException e) {

                    System.out.println(
                            product.getName()
                                    + " | Rejected: "
                                    + e.getMessage()
                    );

                } catch (IllegalArgumentException e) {

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