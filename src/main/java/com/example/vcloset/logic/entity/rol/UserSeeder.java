package com.example.vcloset.logic.entity.rol;

import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createRegularUser();
        this.createSuperAdministrator();
    }

    private void createRegularUser() {
        User regularUser = new User();
        regularUser.setName("Regular");
        regularUser.setLastname("User");
        regularUser.setEmail("johan.araya6@gmail.com");
        regularUser.setPassword("b");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(regularUser.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(regularUser.getName());
        user.setLastname(regularUser.getLastname());
        user.setEmail(regularUser.getEmail());
        user.setPassword(passwordEncoder.encode(regularUser.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);

        User regularUser2 = new User();
        regularUser2.setName("Joshua");
        regularUser2.setLastname("Bolanos");
        regularUser2.setEmail("jbolanosh@ucenfotec.ac.cr");
        regularUser2.setPassword("regularuser123");

        Optional<User> optionalUser2 = userRepository.findByEmail(regularUser2.getEmail());

        if (optionalRole.isEmpty() || optionalUser2.isPresent()) {
            return;
        }

        var user2 = new User();
        user2.setName(regularUser2.getName());
        user2.setLastname(regularUser2.getLastname());
        user2.setEmail(regularUser2.getEmail());
        user2.setPassword(passwordEncoder.encode(regularUser2.getPassword()));
        user2.setRole(optionalRole.get());

        userRepository.save(user2);
    }

    private void createSuperAdministrator() {
        User superAdmin = new User();
        superAdmin.setName("Super");
        superAdmin.setLastname("Admin");
        superAdmin.setEmail("a");
        superAdmin.setPassword("a");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(superAdmin.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(superAdmin.getName());
        user.setLastname(superAdmin.getLastname());
        user.setEmail(superAdmin.getEmail());
        user.setPassword(passwordEncoder.encode(superAdmin.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
