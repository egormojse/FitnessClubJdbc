package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SpaEmployeeProcedure  {

    private int id;

    private SpaEmployee spa_employee;

    private SpaProcedure spa_procedure;

    private int price;

    public SpaEmployeeProcedure() {}
}
