package com.gtmpricingengine.controller;

import com.gtmpricingengine.dto.PricingRequest;
import com.gtmpricingengine.dto.PricingResult;
import com.gtmpricingengine.engine.PricingEngine;
import com.gtmpricingengine.model.ChannelRule;
import com.gtmpricingengine.model.Product;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    private final PricingEngine pricingEngine;


    public PricingController(
            PricingEngine pricingEngine
    ) {
        this.pricingEngine =
                pricingEngine;
    }


    @PostMapping("/calculate")
    public PricingResult calculate(
            @Valid
            @RequestBody
            PricingRequest request
    ) {

        Product product =
                new Product(
                        request.sku(),
                        request.name(),
                        request.rrp(),
                        request.cost()
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