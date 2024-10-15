package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PersonMembership {

    private Integer id;

    private Person person;

    private MembershipType membershipType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer remainingGymVisits;
    private Integer remainingSpaVisits;
}
