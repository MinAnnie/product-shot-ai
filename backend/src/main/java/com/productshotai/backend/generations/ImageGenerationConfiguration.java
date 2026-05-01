package com.productshotai.backend.generations;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ImageGenerationProperties.class)
public class ImageGenerationConfiguration {
}
