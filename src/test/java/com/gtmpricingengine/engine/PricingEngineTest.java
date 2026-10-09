package com.gtmpricingengine.engine;

import com.gtmpricingengine.dto.PricingResult;
import com.gtmpricingengine.model.ChannelRule;
import com.gtmpricingengine.model.Product;
import com.gtmpricingengine.service.PricingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PricingEngineTest {

    @Test
    void shouldCalculateAmazonSellingPriceAndDetectLowMargin() {

        // Arrange
        Product product =
                new Product(
                        "TEST001",
                        "Test Product",
                        100.0,
                        60.0
                );

        ChannelRule channelRule =
                new ChannelRule(
                        "Amazon",
                        0.15,
                        0.30,
                        0.35,
                        0.03,
                        0.15
                );

        PricingService pricingService =
                new PricingService();

        PricingEngine engine =
                new PricingEngine(pricingService);


        // Act
        PricingResult result =
                engine.calculate(
                        product,
                        channelRule
                );


        // Assert
        assertEquals(
                85.00,
                result.getSellingPrice(),
                0.001
        );

        assertEquals(
                0.15,
                result.getDiscount(),
                0.001
        );

        assertEquals(
                "Amazon",
                result.getChannel()
        );

        assertEquals(
                2.55,
                result.getCampaignCost(),
                0.001
        );

        assertEquals(
                0.2641,
                result.getMargin(),
                0.001
        );

        assertFalse(
                result.isMarginValid()
        );
    }


    @Test
    void shouldCalculateBolPriceWhenDiscountMeetsChannelRule() {

        // Arrange
        Product product =
                new Product(
                        "TEST002",
                        "Bol Test Product",
                        100.0,
                        60.0
                );

        ChannelRule channelRule =
                new ChannelRule(
                        "Bol.com",
                        0.10,
                        0.30,
                        0.20,
                        0.03,
                        0.10
                );

        PricingService pricingService =
                new PricingService();

        PricingEngine engine =
                new PricingEngine(pricingService);


        // Act
        PricingResult result =
                engine.calculate(
                        product,
                        channelRule
                );


        // Assert
        assertEquals(
                90.00,
                result.getSellingPrice(),
                0.001
        );

        assertEquals(
                0.10,
                result.getDiscount(),
                0.001
        );

        assertEquals(
                "Bol.com",
                result.getChannel()
        );

        assertEquals(
                2.70,
                result.getCampaignCost(),
                0.001
        );

        assertEquals(
                0.3033,
                result.getMargin(),
                0.001
        );

        assertTrue(
                result.isMarginValid()
        );
    }
}