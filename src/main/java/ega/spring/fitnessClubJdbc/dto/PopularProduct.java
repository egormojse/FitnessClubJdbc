package ega.spring.fitnessClubJdbc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopularProduct {

    private String productName;
    private int count;

    public PopularProduct(String productName, int count) {
        this.productName = productName;
        this.count = count;
    }
}
