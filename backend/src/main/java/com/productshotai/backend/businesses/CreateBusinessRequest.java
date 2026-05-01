package com.productshotai.backend.businesses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateBusinessRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull BusinessType businessType,
        @Size(max = 500) String description,
        @NotNull BrandStyle style,
        @Size(max = 8) List<@NotBlank @Size(max = 40) String> brandColors,
        @Size(max = 8) List<@NotBlank @Size(max = 60) String> moods,
        @Size(max = 60) String defaultChannel
) {
}
