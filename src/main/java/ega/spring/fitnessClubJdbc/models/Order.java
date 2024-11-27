package ega.spring.fitnessClubJdbc.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Order implements Serializable {

    private int id;

    private Person user;

    private Date date;

    private double total_price;

    private String status = "Обрабатывается";

    private boolean deleted;

    private LocalTime time;

    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {}

    public void updateTotalPrice() {
        this.total_price = orderItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }


}
