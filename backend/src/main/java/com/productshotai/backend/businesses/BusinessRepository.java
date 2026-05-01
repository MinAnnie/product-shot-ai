package com.productshotai.backend.businesses;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {
    long countByUserId(UUID userId);
    List<Business> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Business> findByIdAndUserId(UUID id, UUID userId);
}
