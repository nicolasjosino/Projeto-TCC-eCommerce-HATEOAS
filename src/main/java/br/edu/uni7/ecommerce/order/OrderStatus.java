package br.edu.uni7.ecommerce.order;

public enum OrderStatus {

    CREATED, PAID, FULFILLED, CANCELLED;

    static boolean valid(OrderStatus currentStatus, OrderStatus newStatus) {

        if (currentStatus == CREATED) {
            return newStatus == PAID || newStatus == CANCELLED;
        } else if (currentStatus == PAID) {
            return newStatus == FULFILLED;
        } else if (currentStatus == FULFILLED) {
            return false;
        } else if (currentStatus == CANCELLED) {
            return false;
        } else {
            throw new RuntimeException("Unrecognized situation.");
        }
    }
}