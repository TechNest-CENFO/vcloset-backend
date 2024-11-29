package com.example.vcloset.rest.loan;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.ClothingRepository;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.loan.Loan;
import com.example.vcloset.logic.entity.loan.LoanRepository;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/loan")
@RestController
public class LoanRestController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClothingTypeRepository clothingTypeRepository;

    @Autowired
    private ClothingRepository clothingRepository;



    @Autowired
    private GlobalResponseHandler globalResponseHandler;



    @PostMapping("/request")
    public ResponseEntity<?> addLoan(
                @RequestBody Loan loanRequest, HttpServletRequest request) {

        Optional<User> lenderUser = userRepository.findById(loanRequest.getLenderUser().getId());
        if (loanRequest.getLenderUser() == null || loanRequest.getLenderUser().getId() == null) {
            return globalResponseHandler.handleResponse(
                    "Lender user not found with id: " + loanRequest.getLenderUser().getId(),
                    HttpStatus.NOT_FOUND,
                    request);
        }

        Optional<User> loanerUser = userRepository.findById(loanRequest.getLoanerUser().getId());
        if (loanRequest.getLoanerUser() == null || loanRequest.getLoanerUser().getId() == null) {
            return globalResponseHandler.handleResponse(
                    "Loaner user not found with id: " + loanRequest.getLoanerUser().getId(),
                    HttpStatus.NOT_FOUND,
                    request);
        }

        Optional<Clothing> clothing = clothingRepository.findById(loanRequest.getClothing().getId());
        if (clothing.isEmpty()) {
            return globalResponseHandler.handleResponse(
                    "Clothing not found with id: " + loanRequest.getClothing().getId(),
                    HttpStatus.NOT_FOUND,
                    request);
        }

        Loan newLoan = new Loan();
        newLoan.setLenderUser(lenderUser.get());
        newLoan.setLoanerUser(loanerUser.get());
        newLoan.setClothing(clothing.get());
        newLoan.setItemBorrowed(loanRequest.getItemBorrowed());
        newLoan.setLenderScore(loanRequest.getLenderScore());
        newLoan.setLoanerScore(loanRequest.getLoanerScore());

        Loan savedLoan = loanRepository.save(newLoan);

        return globalResponseHandler.handleResponse(
                "Loan created successfully",
                savedLoan,
                HttpStatus.CREATED,
                request);
    }

}
