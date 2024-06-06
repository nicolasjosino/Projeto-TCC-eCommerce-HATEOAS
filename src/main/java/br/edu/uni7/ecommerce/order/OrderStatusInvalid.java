package br.edu.uni7.ecommerce.order;

public class OrderStatusInvalid extends RuntimeException {

    public OrderStatusInvalid(OrderStatus orderStatus) {
        super("Update to status " + orderStatus + " invalid!");
    }
}