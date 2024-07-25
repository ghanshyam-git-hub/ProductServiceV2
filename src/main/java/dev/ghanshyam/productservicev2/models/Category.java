package dev.ghanshyam.productservicev2.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Category extends BaseModel {
    private String category;

    @OneToMany(mappedBy = "category" , cascade = CascadeType.REMOVE)
    private List<Product> productList;
}
