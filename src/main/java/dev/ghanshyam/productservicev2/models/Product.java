package dev.ghanshyam.productservicev2.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends BaseModel {
    private Long product_id;
    private String title;
    private Double price;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn
    private Category category;

    private String description;
    private String imageurl;
}
