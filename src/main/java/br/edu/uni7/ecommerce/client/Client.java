package br.edu.uni7.ecommerce.client;

import br.edu.uni7.ecommerce.order.Order;
import br.edu.uni7.ecommerce.review.Review;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "CLIENTS")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    private String name;
    private String email;
    private String address;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Review> reviews;
}

