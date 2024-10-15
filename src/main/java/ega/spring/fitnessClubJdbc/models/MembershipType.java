package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipType {

    private int id;

    private String type;

    private int gymVisits;

    private int spaVisits;

    private int duration;

    private double price;

    public MembershipType() {}

}