package com.example.hobbify.config;

import com.example.hobbify.bean.core.role.Role;
import com.example.hobbify.bean.core.user.User;
import com.example.hobbify.dao.facade.security.RoleDao;
import com.example.hobbify.dao.facade.security.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Seeds one pre-approved, already-verified demo user on first startup, so a
 * fresh deploy has an account to sign in and explore with immediately —
 * without going through the disabled-until-admin-activates registration flow.
 * Runs after {@link DataInitializer} (Order 2) so roles already exist.
 * Set app.demo-user.enabled=false (DEMO_USER_ENABLED env var) to skip this
 * entirely, e.g. for a production deploy that shouldn't ship a shared login.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class DemoUserSeeder implements CommandLineRunner {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.demo-user.enabled:true}")
    private boolean enabled;

    @Value("${app.demo-user.email:demo@hobbify.com}")
    private String demoEmail;

    @Value("${app.demo-user.password:Demo@12345!}")
    private String demoPassword;

    @Value("${app.demo-user.first-name:Demo}")
    private String demoFirstName;

    @Value("${app.demo-user.last-name:User}")
    private String demoLastName;

    @Override
    @Transactional
    public void run(final String... args) {
        if (!enabled) {
            return;
        }
        if (userDao.existsByEmail(demoEmail)) {
            log.debug("Demo user already exists: {}", demoEmail);
            return;
        }

        final Role userRole = roleDao.findByName("ROLE_USER").orElse(null);
        if (userRole == null) {
            log.warn("ROLE_USER not found. Skipping demo user creation.");
            return;
        }

        final List<Role> roles = new ArrayList<>();
        roles.add(userRole);

        final User demo = User.builder()
                .firstName(demoFirstName)
                .lastName(demoLastName)
                .email(demoEmail)
                .password(passwordEncoder.encode(demoPassword))
                .enabled(true)
                .locked(false)
                .credentialsExpired(false)
                .emailVerified(true)
                .roles(roles)
                .build();

        userDao.save(demo);
        log.info("============================================");
        log.info("  Demo user created successfully");
        log.info("  Email: {}", demoEmail);
        log.info("  Password: (configured via app.demo-user.password / DEMO_USER_PASSWORD)");
        log.info("============================================");
    }
}
