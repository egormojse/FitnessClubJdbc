package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class SpaBooking {

    private int id;

    private Person user;

    private SpaProcedure procedure;

    private int employeeId;

    private String employeeName;

    private Date date;

    private String status;

    private LocalTime time;


    private boolean deleted;

    public SpaBooking() {}

}
