package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GymBooking {

    private int id;

    private Person user;

    private Trainer trainer;

    private LocalDateTime date;

    private String status;

    public GymBooking() {}
}
