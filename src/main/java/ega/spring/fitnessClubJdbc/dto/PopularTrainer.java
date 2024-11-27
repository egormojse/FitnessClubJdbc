package ega.spring.fitnessClubJdbc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopularTrainer {

    private String trainerName;
    private int count;

    public PopularTrainer(String trainerName, int count) {
        this.trainerName = trainerName;
        this.count = count;
    }
}
