package com.gtmpricingengine.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest // set complete spring application context
@AutoConfigureMockMvc  // provide mockmvc to intimate HTTP
class PricingControllerTest {

    @Autowired
    private MockMvc mockMvc;  // spring inserts mockmvc object -dependency injection


    @Test
    void shouldReturnPricingResultForValidRequest()
            throws Exception {

        String requestBody = """
                {
                  "sku": "TEST001",
                  "name": "Test Product",
                  "rrp": 100.0,
                  "cost": 60.0,
                  "channelName": "Amazon",
                  "defaultDiscount": 0.15,
                  "maxDiscount": 0.30,
                  "minimumMargin": 0.35,
                  "campaignFee": 0.03,
                  "minimumCampaignDiscount": 0.15
                }
                """;


        mockMvc.perform(
                        post("/api/pricing/calculate") //POST/api/pricing/calculate
                                .contentType(
                                        MediaType.APPLICATION_JSON  // Content-Type: application/json
                                )
                                .content(requestBody) // HTTP request body
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.channel")
                                .value("Amazon")
                )

                .andExpect(
                        jsonPath("$.sellingPrice")
                                .value(85.0)
                )

                .andExpect(
                        jsonPath("$.discount")
                                .value(0.15)
                )

                .andExpect(
                        jsonPath("$.campaignCost")
                                .value(2.55)
                )

                .andExpect(
                        jsonPath("$.marginValid")
                                .value(false)
                );
    }


    @Test
    void shouldReturnBadRequestWhenDtoValidationFails()
            throws Exception {

        String requestBody = """
                {
                  "sku": "",
                  "name": "Bad Product",
                  "rrp": -100.0,
                  "cost": 60.0,
                  "channelName": "Amazon",
                  "defaultDiscount": 1.5,
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
                        status().isBadRequest()
                )

                .andExpect(
                        jsonPath("$.status")
                                .value(400)
                )

                .andExpect(
                        jsonPath("$.error")
                                .value("Validation failed")
                )

                .andExpect(
                        jsonPath("$.details.sku")
                                .value(
                                        "SKU must not be blank"
                                )
                )

                .andExpect(
                        jsonPath("$.details.rrp")
                                .value(
                                        "RRP must be greater than zero"
                                )
                )

                .andExpect(
                        jsonPath(
                                "$.details.defaultDiscount"
                        )
                                .value(
                                        "Default discount cannot exceed 1"
                                )
                );
    }


    @Test
    void shouldReturnBadRequestWhenDiscountExceedsChannelMaximum()
            throws Exception {

        String requestBody = """
                {
                  "sku": "TEST002",
                  "name": "Invalid Campaign Product",
                  "rrp": 100.0,
                  "cost": 50.0,
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
                )

                .andExpect(
                        jsonPath("$.status")
                                .value(400)
                )

                .andExpect(
                        jsonPath("$.error")
                                .value(
                                        "Invalid pricing request"
                                )
                )

                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Discount is not allowed for channel"
                                )
                );
    }
}