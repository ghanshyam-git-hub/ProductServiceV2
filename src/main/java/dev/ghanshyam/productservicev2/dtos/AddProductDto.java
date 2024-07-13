package dev.ghanshyam.productservicev2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductDto {
    String title;
    Double price;
    String category;
    String description;
    String image;
}
