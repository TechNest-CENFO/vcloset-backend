package com.example.vcloset.logic.entity.season;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class SeasonSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final SeasonRepository seasonRepository;


    public SeasonSeeder(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadSeasons();
    }

    private void loadSeasons() {
        SeasonEnum[] seasonNames = new SeasonEnum[] { SeasonEnum.SUMMER, SeasonEnum.WINTER, SeasonEnum.AUTUMN, SeasonEnum.SPRING };

        Arrays.stream(seasonNames).forEach((seasonName) -> {
            Optional<Season> optionalSeason = seasonRepository.findByName(seasonName);

            optionalSeason.ifPresentOrElse(System.out::println, () -> {
                Season seasonToCreate = new Season();

                seasonToCreate.setName(seasonName);

                seasonRepository.save(seasonToCreate);
            });
        });
    }
}
