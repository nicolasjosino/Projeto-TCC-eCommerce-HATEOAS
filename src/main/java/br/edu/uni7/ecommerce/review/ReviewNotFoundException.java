package br.edu.uni7.ecommerce.review;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long id) {
        super("Could not find review " + id);
    }
}
