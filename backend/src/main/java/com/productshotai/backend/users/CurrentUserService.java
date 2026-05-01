package com.productshotai.backend.users;

import com.productshotai.backend.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private static final String DEV_USER_EMAIL = "demo@productshot.ai";

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(DEV_USER_EMAIL)
                .orElseThrow(() -> new ResourceNotFoundException("Development user was not initialized"));
    }
}
