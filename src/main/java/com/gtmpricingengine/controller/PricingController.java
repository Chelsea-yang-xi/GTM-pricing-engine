package com.gtmpricingengine.controller;

import com.gtmpricingengine.dto.PricingRequest;
import com.gtmpricingengine.dto.PricingResult;
import com.gtmpricingengine.engine.PricingEngine;
import com.gtmpricingengine.model.ChannelRule;
import com.gtmpricingengine.model.Product;
import com.gtmpricingengine.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    private final PricingEngine pricingEngine;

    private final ProductService productService;


    public PricingController(
            PricingEngine pricingEngine,
            ProductService productService
    ) {
        this.pricingEngine =
                pricingEngine;

        this.productService =
                productService;
    }


    @PostMapping("/calculate")
    public PricingResult calculate(
            @Valid
            @RequestBody
            PricingRequest request
    ) {

        Product product =
                productService
                        .getBySku(
                                request.sku()
                        );


        ChannelRule channelRule =
                new ChannelRule(
                        request.channelName(),
                        request.defaultDiscount(),
                        request.maxDiscount(),
                        request.minimumMargin(),
                        request.campaignFee(),
                        request.minimumCampaignDiscount()
                );


        return pricingEngine.calculate(
                product,
                channelRule
        );
    }
}