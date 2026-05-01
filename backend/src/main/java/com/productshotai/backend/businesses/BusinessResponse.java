package com.productshotai.backend.businesses;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BusinessResponse(
        UUID id,
        String name,
        BusinessType businessType,
        String description,
        BrandStyle style,
        List<String> brandColors,
        List<String> moods,
        String defaultChannel,
        Instant createdAt,
        Instant updatedAt
) {
    static BusinessResponse from(Business business) {
        return new BusinessResponse(
                business.getId(),
                business.getName(),
                business.getBusinessType(),
                business.getDescription(),
                business.getStyle(),
                business.getBrandColors(),
                business.getMoods(),
                business.getDefaultChannel(),
                business.getCreatedAt(),
                business.getUpdatedAt()
        );
    }
}
