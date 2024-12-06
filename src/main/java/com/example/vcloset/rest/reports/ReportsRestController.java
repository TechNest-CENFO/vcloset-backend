package com.example.vcloset.rest.reports;


import com.example.vcloset.logic.entity.clothing.ClothingRepository;
import com.example.vcloset.logic.entity.outfit.OutfitRepository;
import com.example.vcloset.logic.entity.reports.DTO.UsersPerMonthDTO;
import com.example.vcloset.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/reports")
@RestController
public class ReportsRestController {
    @Autowired
    private ClothingRepository clothingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OutfitRepository outfitRepository;

    @Transactional
    @GetMapping("/month")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<UsersPerMonthDTO> getAll() {
        List<Object[]> resultList =userRepository.getUsersCreatedLast12Months();
        List<UsersPerMonthDTO> usersPerMonthDTOList = new ArrayList<>();
        for (Object[] row : resultList) {
            Integer year = (row[0] != null) ? (Integer) row[0] : 0;  // Año
            String month = (String) row[1];  // Mes
            Long cant = (Long) row[2];  // Cantidad

            // Crear un nuevo objeto UserCreatedDTO y agregarlo a la lista
            UsersPerMonthDTO usersPerMonthDTO = new UsersPerMonthDTO(year,month,cant);
            usersPerMonthDTOList.add(usersPerMonthDTO);
        }

        return usersPerMonthDTOList;
    }

    @Transactional
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Map<String, Integer> getAllUsersActiveAndInactive(HttpServletRequest request) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Active", userRepository.countAllUsers());
        map.put("Inactive", userRepository.countInactiveUsers());
        map.put("Clothing", clothingRepository.countAllClothing());
        map.put("Outfit", outfitRepository.countAllOutfits());
        return map;
    }
}
