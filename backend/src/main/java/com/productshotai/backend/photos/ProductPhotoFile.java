package com.productshotai.backend.photos;

import org.springframework.core.io.Resource;

public record ProductPhotoFile(ProductPhoto photo, Resource resource) {
}
