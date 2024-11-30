package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class GymBooking  {

    private int id;

    private Person user;

    private Trainer trainer;

    private Date date;

    private String status;

    private boolean deleted;

    private LocalTime time;

    public String getTrainerUsername() {
        return trainer != null ? trainer.getUsername() : "";
    }

    public String getUserUsername() {
        return user != null ? user.getUsername() : "";
    }


    public GymBooking() {}
}
