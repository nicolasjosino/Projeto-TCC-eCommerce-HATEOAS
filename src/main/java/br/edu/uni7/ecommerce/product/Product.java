package br.edu.uni7.ecommerce.product;

import br.edu.uni7.ecommerce.review.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Review> reviews;
}

