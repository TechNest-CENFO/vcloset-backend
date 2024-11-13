package com.example.vcloset.rest.smtp;

import com.example.vcloset.logic.entity.auth.passwordRecovery.ToAddress;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.service.smpt.EmailService;
import com.example.vcloset.logic.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-recovery")
public class PasswordRecoveryController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> sentRecoveryEmail(@RequestBody ToAddress toAddress, HttpServletRequest request) throws MessagingException {
        if(!userService.validateExistingEmail(toAddress.getToAddress())){
            return new GlobalResponseHandler().handleResponse("User not found", HttpStatus.UNAUTHORIZED, request);
        }
        emailService.sendHtmlMessage(toAddress.getToAddress(),"Virtual Closet | Password Recovery");
        return new GlobalResponseHandler().handleResponse("Password recovery email sent", HttpStatus.OK, request);
    }
}
