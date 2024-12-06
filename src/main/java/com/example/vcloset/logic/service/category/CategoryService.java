package com.example.vcloset.logic.service.category;

import com.example.vcloset.logic.entity.category.Category;
import com.example.vcloset.logic.entity.category.CategoryEnum;
import com.example.vcloset.logic.entity.category.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Optional<Category> findByName(CategoryEnum name){
        return categoryRepository.findByName(name);
    }
}
