package com.gtmpricingengine.exception;

import com.gtmpricingengine.dto.ApiErrorResponse;
import com.gtmpricingengine.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // provide consistent exception handling for all controllers
    @ExceptionHandler(
            MethodArgumentNotValidException.class //if this exception occurs then apply this method
    )
    @ResponseStatus(  // HTTP status responds 400
            HttpStatus.BAD_REQUEST
    )
    public ValidationErrorResponse handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> errors =
                new LinkedHashMap<>();


        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(
                        error ->
                                errors.put(
                                        error.getField(),
                                        error.getDefaultMessage()
                                )
                );


        return new ValidationErrorResponse(
                400,
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(
            InvalidDiscountException.class
    )
    @ResponseStatus(
            HttpStatus.BAD_REQUEST
    )
    public ApiErrorResponse handleInvalidDiscount(
            InvalidDiscountException exception
    ) {

        return new ApiErrorResponse(
                400,
                "Invalid pricing request",
                exception.getMessage()
        );

    }
}
