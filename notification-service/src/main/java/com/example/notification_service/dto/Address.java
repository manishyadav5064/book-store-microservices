package com.example.notification_service.dto;

import jakarta.validation.constraints.NotBlank;

public record Address(@NotBlank(message = "addressLine1 is required") String addressLine1,
                      String addressLine2,
                      @NotBlank(message = "city is required") String city,
                      @NotBlank(message = "state is required") String state,
                      @NotBlank(message = "zipcode is required") String zipcode,
                      @NotBlank(message = "country is required") String country) {
}
