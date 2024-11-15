package com.example.vcloset.rest.user;

import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.http.Meta;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<User> ordersPage = userRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(ordersPage.getTotalPages());
        meta.setTotalElements(ordersPage.getTotalElements());
        meta.setPageNumber(ordersPage.getNumber() + 1);
        meta.setPageSize(ordersPage.getSize());

        return new GlobalResponseHandler().handleResponse("Order retrieved successfully",
                ordersPage.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new GlobalResponseHandler().handleResponse("User updated successfully",
                user, HttpStatus.OK, request);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if(foundOrder.isPresent()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new GlobalResponseHandler().handleResponse("User updated successfully",
                    user, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if(foundOrder.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse("User deleted successfully",
                    foundOrder.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Order id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @PutMapping("/profile/{userId}")
    @PreAuthorize("isAuthenticated()")
    public Optional<User> updateUserProfile(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {

        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setPicture(user.getPicture());
                    existingUser.setName(user.getName());
                    existingUser.setLastname(user.getLastname());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setDateOfBirth(user.getDateOfBirth());
                    existingUser.setDirection(user.getDirection());

                    return userRepository.save(existingUser);
                });
    }


    @PatchMapping("/profile/{userId}")
    @PreAuthorize("isAuthenticated()")
    public Optional<User> deleteUserAccount(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {

        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setIsUserActive(user.isIsUserActive());
                    return userRepository.save(existingUser);
                });
    }

    @PatchMapping("/profile/privacy/{userId}")
    @PreAuthorize("isAuthenticated()")
    public Optional<User> setProfilePrivacy(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {

        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setIsProfileBlocked(user.isIsProfileBlocked());
                    return userRepository.save(existingUser);
                });
    }

    @PatchMapping("/profile/picture/{userId}")
    @PreAuthorize("isAuthenticated()")
    public Optional<User> updateProfilePicture(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {

        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setPicture(user.getPicture());
                    return userRepository.save(existingUser);
                });
    }


    @PatchMapping("/profile/password/{userId}")
    @PreAuthorize("isAuthenticated()")
    public Optional<User> updateUserPassword(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {

        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepository.save(existingUser);
                });
    }
}