package com.productshotai.backend.generations;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/photos/{photoId}/generations")
public class ImageGenerationController {

    private final ImageGenerationService imageGenerationService;

    public ImageGenerationController(ImageGenerationService imageGenerationService) {
        this.imageGenerationService = imageGenerationService;
    }

    @PostMapping
    public ImageGenerationResult generate(@PathVariable UUID photoId) {
        return imageGenerationService.generate(photoId);
    }
}
