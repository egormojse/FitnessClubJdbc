package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SpaBooking {

    private int id;

    private Person user;

    private SpaProcedure procedure;

    private int employeeId;

    private LocalDateTime date;

    private String status;

    public SpaBooking() {}

}
