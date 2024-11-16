package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderItem  {

    private int id;

    private Order order;

    private Product product;

    private int quantity;

    private double price;

    public OrderItem() {}
}
