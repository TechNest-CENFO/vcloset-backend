package com.example.vcloset.rest.smtp;

import com.example.vcloset.logic.entity.auth.passwordResetEntity.PasswordResetEntity;
import com.example.vcloset.logic.entity.auth.passwordResetEntity.PasswordResetEntityRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/password-reset")
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetEntityRepository passwordResetEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping()
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetEntity passwordResetEntity, HttpServletRequest request) throws MessagingException {

        PasswordResetEntity storedPasswordResetEntity = passwordResetEntityRepository.findByToken(passwordResetEntity.getToken());

        if (storedPasswordResetEntity == null || storedPasswordResetEntity.getExpirationDate().before(new Date())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El enlace de restablecimiento de contraseña es inválido o ha expirado.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(storedPasswordResetEntity.getEmail());

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setPassword(passwordEncoder.encode(passwordResetEntity.getNewPassword()));
            userRepository.save(existingUser);
        }

        passwordResetEntityRepository.delete(storedPasswordResetEntity);

        return new GlobalResponseHandler().handleResponse("Password recovery email sent", HttpStatus.OK, request);
    }
}
