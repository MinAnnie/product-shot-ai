package com.productshotai.backend.generations;

import com.productshotai.backend.businesses.Business;
import com.productshotai.backend.photos.ProductPhoto;

public record ImageGenerationRequest(
        Business business,
        ProductPhoto productPhoto,
        String productImageUrl,
        String prompt
) {
}
