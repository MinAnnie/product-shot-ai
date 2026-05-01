package com.productshotai.backend.photos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, UUID> {
    List<ProductPhoto> findAllByBusinessIdOrderByCreatedAtDesc(UUID businessId);
    Optional<ProductPhoto> findByIdAndBusinessUserId(UUID id, UUID userId);
}
