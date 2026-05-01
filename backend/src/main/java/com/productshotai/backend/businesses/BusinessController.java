package com.productshotai.backend.businesses;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BusinessResponse create(@Valid @RequestBody CreateBusinessRequest request) {
        return businessService.create(request);
    }

    @GetMapping
    public List<BusinessResponse> findAll() {
        return businessService.findAllForCurrentUser();
    }

    @GetMapping("/{id}")
    public BusinessResponse findById(@PathVariable UUID id) {
        return businessService.findByIdForCurrentUser(id);
    }
}
