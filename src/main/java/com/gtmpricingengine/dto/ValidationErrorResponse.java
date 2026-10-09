package com.gtmpricingengine.dto;

import java.util.Map;

public record ValidationErrorResponse(

        int status,

        String error,

        Map<String, String> details

) {
}