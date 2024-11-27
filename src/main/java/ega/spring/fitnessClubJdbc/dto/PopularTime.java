package ega.spring.fitnessClubJdbc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopularTime {

    private String time;
    private int count;

    public PopularTime(String time, int count) {
        this.time = time;
        this.count = count;
    }
}
