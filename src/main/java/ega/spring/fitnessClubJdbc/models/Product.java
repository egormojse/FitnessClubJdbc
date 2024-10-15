package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private int id;

    private String name;

    private String description;

    private double price;

    private String category;

    private int stock;

    public Product() {}



}
