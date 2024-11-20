package com.example.vcloset.logic.entity.category;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CategorySeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;


    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadCategorys();
    }

    private void loadCategorys() {
        CategoryEnum[] categoryNames = new CategoryEnum[]
                {
                CategoryEnum.CASUAL,
                CategoryEnum.FORMAL,
                CategoryEnum.SEMI_FORMAL,
                CategoryEnum.DEPORTIVO,
                CategoryEnum.PLAYERO,
                CategoryEnum.VIAJE,
                CategoryEnum.FESTIVAL,
                CategoryEnum.CALLEJERO,
                CategoryEnum.OTRO
                };

        Arrays.stream(categoryNames).forEach((categoryName) -> {
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);

            optionalCategory.ifPresentOrElse(System.out::println, () -> {
                Category categoryToCreate = new Category();

                categoryToCreate.setName(categoryName);

                categoryRepository.save(categoryToCreate);
            });
        });
    }
}
