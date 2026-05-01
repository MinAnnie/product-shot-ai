package com.productshotai.backend.generations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ai")
public class ImageGenerationProperties {
    private String provider = "fal";
    private String publicBaseUrl = "http://localhost:8080";
    private final Fal fal = new Fal();

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getPublicBaseUrl() { return publicBaseUrl; }
    public void setPublicBaseUrl(String publicBaseUrl) { this.publicBaseUrl = publicBaseUrl; }
    public Fal getFal() { return fal; }

    public static class Fal {
        private String apiKey = "";
        private String baseUrl = "https://fal.run";
        private String endpointId = "fal-ai/flux-pro/kontext";
        private String outputFormat = "jpeg";
        private String aspectRatio = "1:1";
        private String safetyTolerance = "2";
        private boolean enhancePrompt = true;
        private double guidanceScale = 3.5;
        private int numImages = 1;

        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getEndpointId() { return endpointId; }
        public void setEndpointId(String endpointId) { this.endpointId = endpointId; }
        public String getOutputFormat() { return outputFormat; }
        public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
        public String getAspectRatio() { return aspectRatio; }
        public void setAspectRatio(String aspectRatio) { this.aspectRatio = aspectRatio; }
        public String getSafetyTolerance() { return safetyTolerance; }
        public void setSafetyTolerance(String safetyTolerance) { this.safetyTolerance = safetyTolerance; }
        public boolean isEnhancePrompt() { return enhancePrompt; }
        public void setEnhancePrompt(boolean enhancePrompt) { this.enhancePrompt = enhancePrompt; }
        public double getGuidanceScale() { return guidanceScale; }
        public void setGuidanceScale(double guidanceScale) { this.guidanceScale = guidanceScale; }
        public int getNumImages() { return numImages; }
        public void setNumImages(int numImages) { this.numImages = numImages; }
    }
}
