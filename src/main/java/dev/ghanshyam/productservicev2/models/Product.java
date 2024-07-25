package dev.ghanshyam.productservicev2.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends BaseModel  { // for redis storage should have implemented Serializable but becoz we are extending BaseModel we need not implement that additionally
    private Long productId;
    private String title;
    private Double price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;

    private String description;
    private String imageurl;
}
