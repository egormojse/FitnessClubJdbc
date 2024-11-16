package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class PersonMembership {

    private Integer id;

    private Person person;

    private MembershipType membershipType;

    private Date startDate;

    private Date endDate;

    private Integer remainingGymVisits;
    private Integer remainingSpaVisits;
}
