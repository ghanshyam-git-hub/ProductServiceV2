package dev.ghanshyam.productservicev2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto implements Serializable { // Serializable for Redis storage , so that redis can pack and unpack in this class Format
    private Long id;
    private String title;
    private Double price;
    private String category;
    private String description;
    private String image;
}
