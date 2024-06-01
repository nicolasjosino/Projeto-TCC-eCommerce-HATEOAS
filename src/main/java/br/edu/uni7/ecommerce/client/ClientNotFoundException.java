package br.edu.uni7.ecommerce.client;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Could not find client " + id);
    }
}