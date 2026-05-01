package com.productshotai.backend.businesses;

import com.productshotai.backend.users.User;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "businesses")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", nullable = false, length = 40)
    private BusinessType businessType;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private BrandStyle style;

    @ElementCollection
    @CollectionTable(name = "business_brand_colors", joinColumns = @JoinColumn(name = "business_id"))
    @Column(name = "color", nullable = false, length = 40)
    private List<String> brandColors = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "business_moods", joinColumns = @JoinColumn(name = "business_id"))
    @Column(name = "mood", nullable = false, length = 60)
    private List<String> moods = new ArrayList<>();

    @Column(name = "default_channel", length = 60)
    private String defaultChannel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Business() {
    }

    public Business(User user, String name, BusinessType businessType, String description, BrandStyle style,
                    List<String> brandColors, List<String> moods, String defaultChannel) {
        this.user = user;
        this.name = name;
        this.businessType = businessType;
        this.description = description;
        this.style = style;
        this.brandColors = brandColors == null ? new ArrayList<>() : new ArrayList<>(brandColors);
        this.moods = moods == null ? new ArrayList<>() : new ArrayList<>(moods);
        this.defaultChannel = defaultChannel;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public User getUser() { return user; }
    public String getName() { return name; }
    public BusinessType getBusinessType() { return businessType; }
    public String getDescription() { return description; }
    public BrandStyle getStyle() { return style; }
    public List<String> getBrandColors() { return List.copyOf(brandColors); }
    public List<String> getMoods() { return List.copyOf(moods); }
    public String getDefaultChannel() { return defaultChannel; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
