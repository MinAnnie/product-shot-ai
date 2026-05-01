package com.productshotai.backend.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DevelopmentDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        userRepository.findByEmail("demo@productshot.ai")
                .orElseGet(() -> userRepository.save(new User(
                        "Demo User",
                        "demo@productshot.ai",
                        "dev-password-not-for-production",
                        PlanType.FREE
                )));
    }
}
