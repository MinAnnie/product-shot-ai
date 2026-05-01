package com.productshotai.backend.generations;

import com.productshotai.backend.common.ResourceNotFoundException;
import com.productshotai.backend.photos.ProductPhoto;
import com.productshotai.backend.photos.ProductPhotoRepository;
import com.productshotai.backend.users.CurrentUserService;
import com.productshotai.backend.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ImageGenerationService {

    private final ProductPhotoRepository productPhotoRepository;
    private final CurrentUserService currentUserService;
    private final ImageGenerationProvider imageGenerationProvider;
    private final ImageGenerationProperties properties;

    public ImageGenerationService(ProductPhotoRepository productPhotoRepository,
                                  CurrentUserService currentUserService,
                                  ImageGenerationProvider imageGenerationProvider,
                                  ImageGenerationProperties properties) {
        this.productPhotoRepository = productPhotoRepository;
        this.currentUserService = currentUserService;
        this.imageGenerationProvider = imageGenerationProvider;
        this.properties = properties;
    }

    @Transactional(readOnly = true)
    public ImageGenerationResult generate(UUID photoId) {
        User user = currentUserService.getCurrentUser();
        ProductPhoto photo = productPhotoRepository.findByIdAndBusinessUserId(photoId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product photo not found"));

        String prompt = buildPrompt(photo);
        String imageUrl = publicBaseUrl() + "/api/photos/" + photo.getId() + "/file";
        return imageGenerationProvider.generate(new ImageGenerationRequest(photo.getBusiness(), photo, imageUrl, prompt));
    }

    private String buildPrompt(ProductPhoto photo) {
        var business = photo.getBusiness();
        return "Create a professional product photo background for a " + business.getBusinessType().name().toLowerCase().replace('_', ' ') +
                " business named " + business.getName() + ". Keep the original product unchanged and clearly visible. " +
                "Use a " + business.getStyle().name().toLowerCase().replace('_', ' ') + " brand style. " +
                optional("Business description: ", business.getDescription()) +
                optionalList("Brand colors: ", business.getBrandColors()) +
                optionalList("Mood: ", business.getMoods()) +
                optional("Target channel: ", business.getDefaultChannel()) +
                "Generate a clean, commercial, high-quality image suitable for ecommerce and social media.";
    }

    private String publicBaseUrl() {
        String publicBaseUrl = properties.getPublicBaseUrl();
        return publicBaseUrl.endsWith("/") ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1) : publicBaseUrl;
    }

    private String optional(String label, String value) {
        return value == null || value.isBlank() ? "" : label + value + ". ";
    }

    private String optionalList(String label, java.util.List<String> values) {
        return values == null || values.isEmpty() ? "" : label + String.join(", ", values) + ". ";
    }
}
