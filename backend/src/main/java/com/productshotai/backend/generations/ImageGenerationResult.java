package com.productshotai.backend.generations;

import java.util.List;

public record ImageGenerationResult(
        String provider,
        String model,
        String prompt,
        List<GeneratedImage> images
) {
    public record GeneratedImage(String url, Integer width, Integer height, String contentType) {
    }
}
