package br.edu.uni7.ecommerce.client;

import br.edu.uni7.ecommerce.order.Order;
import br.edu.uni7.ecommerce.review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        if (client != null) {
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client newClient = clientService.saveClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Client existingClient = clientService.getClientById(id);
        if (existingClient != null) {
            client.setClientId(id);
            Client updatedClient = clientService.saveClient(client);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } else {
            Client createdClient = clientService.saveClient(client);
            return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getAllOrdersByClientId(@PathVariable Long id) {
        List<Order> orders = clientService.getAllOrdersByClientId(id);
        if (orders != null && !orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(orders, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getAllReviewsByClientId(@PathVariable Long id) {
        List<Review> reviews = clientService.getAllReviewsByClientId(id);
        if (reviews != null && !reviews.isEmpty()) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }
    }
}