package br.edu.uni7.ecommerce.order;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Date orderDate;
    private String orderStatus;
    private Long clientId;
    private List<OrderItemDTO> orderItems;

    @Data
    public static class OrderItemDTO {
        private Long productId;
        private int quantity;
    }
}
