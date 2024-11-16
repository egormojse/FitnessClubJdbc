package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SpaProcedure  {

    private int id;

    private String name;

    private String description;

    private int duration;

    private String type;

    public SpaProcedure() {

    }
}
