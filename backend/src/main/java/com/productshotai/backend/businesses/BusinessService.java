package com.productshotai.backend.businesses;

import com.productshotai.backend.common.BusinessRuleException;
import com.productshotai.backend.common.ResourceNotFoundException;
import com.productshotai.backend.users.CurrentUserService;
import com.productshotai.backend.users.PlanType;
import com.productshotai.backend.users.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final CurrentUserService currentUserService;

    public BusinessService(BusinessRepository businessRepository, CurrentUserService currentUserService) {
        this.businessRepository = businessRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public BusinessResponse create(CreateBusinessRequest request) {
        User user = currentUserService.getCurrentUser();
        enforceBusinessLimit(user);

        Business business = new Business(
                user,
                request.name(),
                request.businessType(),
                request.description(),
                request.style(),
                request.brandColors(),
                request.moods(),
                request.defaultChannel()
        );

        return BusinessResponse.from(businessRepository.save(business));
    }

    @Transactional(readOnly = true)
    public List<BusinessResponse> findAllForCurrentUser() {
        User user = currentUserService.getCurrentUser();
        return businessRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(BusinessResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BusinessResponse findByIdForCurrentUser(UUID id) {
        User user = currentUserService.getCurrentUser();
        return businessRepository.findByIdAndUserId(id, user.getId())
                .map(BusinessResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
    }

    private void enforceBusinessLimit(User user) {
        long currentBusinesses = businessRepository.countByUserId(user.getId());
        int limit = user.getPlan() == PlanType.PRO ? 5 : 1;

        if (currentBusinesses >= limit) {
            throw new BusinessRuleException("Your current plan allows up to " + limit + " business profile(s)");
        }
    }
}
