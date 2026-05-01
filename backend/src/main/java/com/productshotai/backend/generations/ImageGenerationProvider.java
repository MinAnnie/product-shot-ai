package com.productshotai.backend.generations;

public interface ImageGenerationProvider {
    String providerName();
    ImageGenerationResult generate(ImageGenerationRequest request);
}
