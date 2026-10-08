package com.gtmpricingengine.dto;

public record ApiErrorResponse(

        int status,

        String error,

        String message

) {
}