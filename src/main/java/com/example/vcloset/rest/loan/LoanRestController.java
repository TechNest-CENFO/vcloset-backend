package com.example.vcloset.rest.loan;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.ClothingRepository;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.http.Meta;
import com.example.vcloset.logic.entity.loan.Loan;
import com.example.vcloset.logic.entity.loan.LoanRepository;
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
        newLoan.setItemRequested(true);
        newLoan.setLenderScore(loanRequest.getLenderScore());
        newLoan.setLoanerScore(loanRequest.getLoanerScore());

        Loan savedLoan = loanRepository.save(newLoan);

        return globalResponseHandler.handleResponse(
                "Loan created successfully",
                savedLoan,
                HttpStatus.CREATED,
                request);
    }

    @GetMapping("{userId}/public")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllPublicClothing(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long userId,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Clothing> clothingPage = loanRepository.findByPublicClothingItem(userId, pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(clothingPage.getTotalPages());
        meta.setTotalElements(clothingPage.getTotalElements());
        meta.setPageNumber(clothingPage.getNumber() + 1);
        meta.setPageSize(clothingPage.getSize());
        return new GlobalResponseHandler().handleResponse("Clothing Items retrieved successfully",
                clothingPage.getContent(), HttpStatus.OK, meta);
    }


    @GetMapping("{userId}/my-requests")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyRequests(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long userId,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Clothing> clothingPage = loanRepository.findMyRequests(userId, pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(clothingPage.getTotalPages());
        meta.setTotalElements(clothingPage.getTotalElements());
        meta.setPageNumber(clothingPage.getNumber() + 1);
        meta.setPageSize(clothingPage.getSize());
        return new GlobalResponseHandler().handleResponse("Clothing Items retrieved successfully",
                clothingPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("{userId}/my-loans")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyLoans(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long userId,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Clothing> clothingPage = loanRepository.findByPublicClothingItem(userId, pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(clothingPage.getTotalPages());
        meta.setTotalElements(clothingPage.getTotalElements());
        meta.setPageNumber(clothingPage.getNumber() + 1);
        meta.setPageSize(clothingPage.getSize());
        return new GlobalResponseHandler().handleResponse("Clothing Items retrieved successfully",
                clothingPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("{userId}/my-lends")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyLends(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long userId,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Clothing> clothingPage = loanRepository.findByPublicClothingItem(userId, pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(clothingPage.getTotalPages());
        meta.setTotalElements(clothingPage.getTotalElements());
        meta.setPageNumber(clothingPage.getNumber() + 1);
        meta.setPageSize(clothingPage.getSize());
        return new GlobalResponseHandler().handleResponse("Clothing Items retrieved successfully",
                clothingPage.getContent(), HttpStatus.OK, meta);
    }
}
