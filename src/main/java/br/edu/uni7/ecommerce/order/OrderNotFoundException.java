package br.edu.uni7.ecommerce.order;

class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("Order " + id + " not found!");
    }
}