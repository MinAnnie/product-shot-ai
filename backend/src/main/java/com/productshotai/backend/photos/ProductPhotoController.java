package com.productshotai.backend.photos;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class ProductPhotoController {

    private final ProductPhotoService productPhotoService;

    public ProductPhotoController(ProductPhotoService productPhotoService) {
        this.productPhotoService = productPhotoService;
    }

    @PostMapping(value = "/businesses/{businessId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductPhotoResponse upload(@PathVariable UUID businessId, @RequestParam("file") MultipartFile file) {
        return productPhotoService.upload(businessId, file);
    }

    @GetMapping("/businesses/{businessId}/photos")
    public List<ProductPhotoResponse> findAll(@PathVariable UUID businessId) {
        return productPhotoService.findAllForBusiness(businessId);
    }

    @GetMapping("/photos/{photoId}/file")
    public ResponseEntity<?> file(@PathVariable UUID photoId) {
        ProductPhotoFile file = productPhotoService.loadFile(photoId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.photo().getContentType()))
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(file.resource());
    }
}
