package com.productshotai.backend.generations;

import com.productshotai.backend.common.BusinessRuleException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(prefix = "app.ai", name = "provider", havingValue = "fal", matchIfMissing = true)
public class FalImageGenerationProvider implements ImageGenerationProvider {

    private final ImageGenerationProperties properties;
    private final RestClient restClient;

    public FalImageGenerationProvider(ImageGenerationProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = restClientBuilder.build();
    }

    @Override
    public String providerName() {
        return "fal";
    }

    @Override
    public ImageGenerationResult generate(ImageGenerationRequest request) {
        ImageGenerationProperties.Fal fal = properties.getFal();
        if (!StringUtils.hasText(fal.getApiKey())) {
            throw new BusinessRuleException("FAL_KEY is not configured");
        }

        Map<String, Object> body = Map.of(
                "prompt", request.prompt(),
                "image_url", request.productImageUrl(),
                "guidance_scale", fal.getGuidanceScale(),
                "num_images", fal.getNumImages(),
                "output_format", fal.getOutputFormat(),
                "safety_tolerance", fal.getSafetyTolerance(),
                "enhance_prompt", fal.isEnhancePrompt(),
                "aspect_ratio", fal.getAspectRatio()
        );

        FalResponse response = restClient.post()
                .uri(normalizedEndpointUrl(fal))
                .header("Authorization", "Key " + fal.getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(FalResponse.class);

        if (response == null || response.images() == null || response.images().isEmpty()) {
            throw new BusinessRuleException("fal.ai did not return generated images");
        }

        List<ImageGenerationResult.GeneratedImage> images = response.images().stream()
                .map(image -> new ImageGenerationResult.GeneratedImage(image.url(), image.width(), image.height(), image.content_type()))
                .toList();

        return new ImageGenerationResult(providerName(), fal.getEndpointId(), response.prompt(), images);
    }

    private String normalizedEndpointUrl(ImageGenerationProperties.Fal fal) {
        String baseUrl = fal.getBaseUrl().endsWith("/") ? fal.getBaseUrl().substring(0, fal.getBaseUrl().length() - 1) : fal.getBaseUrl();
        String endpoint = fal.getEndpointId().startsWith("/") ? fal.getEndpointId().substring(1) : fal.getEndpointId();
        return baseUrl + "/" + endpoint;
    }

    private record FalResponse(List<FalImage> images, String prompt) {
    }

    private record FalImage(String url, Integer width, Integer height, String content_type) {
    }
}
