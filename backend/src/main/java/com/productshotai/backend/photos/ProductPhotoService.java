package com.productshotai.backend.photos;

import com.productshotai.backend.businesses.Business;
import com.productshotai.backend.businesses.BusinessRepository;
import com.productshotai.backend.common.BusinessRuleException;
import com.productshotai.backend.common.ResourceNotFoundException;
import com.productshotai.backend.users.CurrentUserService;
import com.productshotai.backend.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductPhotoService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024;

    private final BusinessRepository businessRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final CurrentUserService currentUserService;
    private final Path uploadDir;

    public ProductPhotoService(BusinessRepository businessRepository,
                               ProductPhotoRepository productPhotoRepository,
                               CurrentUserService currentUserService,
                               @Value("${app.storage.upload-dir:uploads}") String uploadDir) {
        this.businessRepository = businessRepository;
        this.productPhotoRepository = productPhotoRepository;
        this.currentUserService = currentUserService;
        this.uploadDir = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    @Transactional
    public ProductPhotoResponse upload(UUID businessId, MultipartFile file) {
        User user = currentUserService.getCurrentUser();
        Business business = businessRepository.findByIdAndUserId(businessId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateFile(file);

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "product-photo" : file.getOriginalFilename());
        String extension = resolveExtension(file.getContentType());
        String storedFilename = UUID.randomUUID() + extension;

        try {
            Files.createDirectories(uploadDir);
            file.transferTo(uploadDir.resolve(storedFilename));
        } catch (IOException exception) {
            throw new BusinessRuleException("Could not store product photo");
        }

        ProductPhoto photo = new ProductPhoto(business, originalFilename, storedFilename, file.getContentType(), file.getSize());
        return ProductPhotoResponse.from(productPhotoRepository.save(photo));
    }

    @Transactional(readOnly = true)
    public List<ProductPhotoResponse> findAllForBusiness(UUID businessId) {
        User user = currentUserService.getCurrentUser();
        if (businessRepository.findByIdAndUserId(businessId, user.getId()).isEmpty()) {
            throw new ResourceNotFoundException("Business not found");
        }
        return productPhotoRepository.findAllByBusinessIdOrderByCreatedAtDesc(businessId).stream()
                .map(ProductPhotoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductPhotoFile loadFile(UUID photoId) {
        User user = currentUserService.getCurrentUser();
        ProductPhoto photo = productPhotoRepository.findByIdAndBusinessUserId(photoId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product photo not found"));
        try {
            Resource resource = new UrlResource(uploadDir.resolve(photo.getStoredFilename()).normalize().toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Product photo file not found");
            }
            return new ProductPhotoFile(photo, resource);
        } catch (MalformedURLException exception) {
            throw new ResourceNotFoundException("Product photo file not found");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessRuleException("Please select a product photo");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new BusinessRuleException("Product photo must be 10MB or less");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new BusinessRuleException("Only JPG, PNG, or WebP product photos are supported");
        }
    }

    private String resolveExtension(String contentType) {
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }
}
