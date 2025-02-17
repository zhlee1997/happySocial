package com.jeremylee.insta_user_service.config;

import com.jeremylee.insta_user_service.model.Role;
import com.jeremylee.insta_user_service.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RoleRepo roleRepo;

    @Override
    public void run(String... args) throws Exception {
        Set<String> defaultRoles =  Set.of("ROLE_ADMIN", "ROLE_USER");

        defaultRoles.stream()
                .filter(role -> roleRepo.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepo::save);
    }
}
