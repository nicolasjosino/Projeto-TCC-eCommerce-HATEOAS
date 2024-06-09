package br.edu.uni7.ecommerce.review;

import br.edu.uni7.ecommerce.client.Client;
import br.edu.uni7.ecommerce.product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Entity
@Data
@Table(name = "REVIEWS")
public class Review extends RepresentationModel<Review> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    private int rating;
    private String comment;
    private Date reviewDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIncludeProperties(value = {"clientId"})
    private Client client;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIncludeProperties(value = {"productId"})
    private Product product;
}
