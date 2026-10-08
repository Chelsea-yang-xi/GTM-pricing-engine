package com.gtmpricingengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = "com.gtmpricingengine"
)
public class GtmPricingEngineApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                GtmPricingEngineApplication.class,
                args
        );
    }
}