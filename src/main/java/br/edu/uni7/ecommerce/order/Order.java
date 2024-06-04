package br.edu.uni7.ecommerce.order;

import br.edu.uni7.ecommerce.client.Client;
import br.edu.uni7.ecommerce.orderitem.OrderItem;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Date orderDate;
    private String orderStatus;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIncludeProperties(value = {"clientId"})
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItems;
}

