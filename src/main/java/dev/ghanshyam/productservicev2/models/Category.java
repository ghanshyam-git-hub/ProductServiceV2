package dev.ghanshyam.productservicev2.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Category extends BaseModel {
    private String category;

    @OneToMany(mappedBy = "category" , cascade = CascadeType.REMOVE)
    private List<Product> productList;
}
