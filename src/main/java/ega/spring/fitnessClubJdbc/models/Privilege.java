package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Privilege {

    private Integer id;

    private String name;
    private String description;

    public Privilege() {}
}
