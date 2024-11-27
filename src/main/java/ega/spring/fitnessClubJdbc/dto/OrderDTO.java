package ega.spring.fitnessClubJdbc.dto;

import ega.spring.fitnessClubJdbc.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private int orderId;
    private Date orderDate;
    private LocalTime time;
    private Double totalPrice;
    private String userName;
    private List<Product> products;
    private String status;

}
