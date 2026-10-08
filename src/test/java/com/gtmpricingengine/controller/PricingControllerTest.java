package com.gtmpricingengine.controller;

import com.gtmpricingengine.model.Product;
import com.gtmpricingengine.repository.SpringDataProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PricingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataProductRepository productRepository;


    @BeforeEach
    void setUp() {

        productRepository.deleteAll();

        productRepository.save(
                new Product(
                        "TEST001",
                        "Test Product",
                        100.0,
                        60.0
                )
        );
    }


    @Test
    void shouldCalculatePricingUsingStoredProduct()
            throws Exception {

        String requestBody = """
                {
                  "sku": "TEST001",
                  "channelName": "Amazon",
                  "defaultDiscount": 0.15,
                  "maxDiscount": 0.30,
                  "minimumMargin": 0.35,
                  "campaignFee": 0.03,
                  "minimumCampaignDiscount": 0.15
                }
                """;

        mockMvc.perform(
                        post("/api/pricing/calculate")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.product.sku")
                                .value("TEST001")
                )
                .andExpect(
                        jsonPath("$.sellingPrice")
                                .value(85.0)
                )
                .andExpect(
                        jsonPath("$.channel")
                                .value("Amazon")
                );
    }


    @Test
    void shouldReturnNotFoundForUnknownSku()
            throws Exception {

        String requestBody = """
                {
                  "sku": "UNKNOWN",
                  "channelName": "Amazon",
                  "defaultDiscount": 0.15,
                  "maxDiscount": 0.30,
                  "minimumMargin": 0.35,
                  "campaignFee": 0.03,
                  "minimumCampaignDiscount": 0.15
                }
                """;

        mockMvc.perform(
                        post("/api/pricing/calculate")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                );
    }


    @Test
    void shouldRejectDiscountAboveMaximum()
            throws Exception {

        String requestBody = """
                {
                  "sku": "TEST001",
                  "channelName": "Amazon",
                  "defaultDiscount": 0.40,
                  "maxDiscount": 0.30,
                  "minimumMargin": 0.20,
                  "campaignFee": 0.03,
                  "minimumCampaignDiscount": 0.15
                }
                """;

        mockMvc.perform(
                        post("/api/pricing/calculate")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                )
                .andExpect(
                        status().isBadRequest()
                );
    }
}