package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Order {

    private int id;

    private Person user;

    private Date date;

    private double total_price;

    private String status = "Обрабатывается";

    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {}


}
