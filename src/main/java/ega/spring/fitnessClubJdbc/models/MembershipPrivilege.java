package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipPrivilege {

    private MembershipType membershipType;

    private Privilege privilege;

}


