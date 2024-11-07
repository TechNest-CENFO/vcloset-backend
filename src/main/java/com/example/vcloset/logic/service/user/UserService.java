package com.example.vcloset.logic.service.user;

import com.example.vcloset.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean validateExistingEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
