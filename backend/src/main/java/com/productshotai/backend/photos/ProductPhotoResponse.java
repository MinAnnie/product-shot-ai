package com.productshotai.backend.photos;

import java.time.Instant;
import java.util.UUID;

public record ProductPhotoResponse(
        UUID id,
        UUID businessId,
        String originalFilename,
        String contentType,
        long sizeBytes,
        String url,
        Instant createdAt
) {
    public static ProductPhotoResponse from(ProductPhoto photo) {
        return new ProductPhotoResponse(
                photo.getId(),
                photo.getBusiness().getId(),
                photo.getOriginalFilename(),
                photo.getContentType(),
                photo.getSizeBytes(),
                "/api/photos/" + photo.getId() + "/file",
                photo.getCreatedAt()
        );
    }
}
