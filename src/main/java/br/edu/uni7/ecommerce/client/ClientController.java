package br.edu.uni7.ecommerce.client;

import br.edu.uni7.ecommerce.order.Order;
import br.edu.uni7.ecommerce.order.OrderController;
import br.edu.uni7.ecommerce.review.Review;
import br.edu.uni7.ecommerce.review.ReviewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping(produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<Client>> getAllClients() {
        List<Client> allClients = clientService.getAllClients();
        if (allClients.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            for (Client client : allClients) {
                client.add(linkTo(methodOn(ClientController.class).getClientById(client.getClientId())).withSelfRel());
                client.add(linkTo(methodOn(ClientController.class).getAllOrdersByClientId(client.getClientId())).withRel("orders"));
                client.add(linkTo(methodOn(ClientController.class).getAllReviewsByClientId(client.getClientId())).withRel("reviews"));
            }
            CollectionModel<Client> clientCollectionModel = CollectionModel.of(allClients);
            clientCollectionModel.add(linkTo(methodOn(ClientController.class).getAllClients()).withSelfRel());
            return new ResponseEntity<>(clientCollectionModel, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<EntityModel<Client>> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        if (client == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        EntityModel<Client> entityModel = generateLinks(client, id);
        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }

    @PostMapping(produces = {"application/hal+json"})
    public ResponseEntity<EntityModel<Client>> createClient(@RequestBody Client client) {
        Client newClient = clientService.saveClient(client);
        EntityModel<Client> entityModel = generateLinks(newClient, newClient.getClientId());
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<EntityModel<Client>> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Client existingClient = clientService.getClientById(id);
        if (existingClient != null) {
            client.setClientId(id);
            Client updatedClient = clientService.saveClient(client);
            EntityModel<Client> entityModel = generateLinks(updatedClient, id);
            return new ResponseEntity<>(entityModel, HttpStatus.OK);
        } else {
            Client createdClient = clientService.saveClient(client);
            EntityModel<Client> entityModel = generateLinks(createdClient, createdClient.getClientId());
            return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
        }
    }

    @DeleteMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/orders", produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<Order>> getAllOrdersByClientId(@PathVariable Long id) {
        List<Order> orders = clientService.getAllOrdersByClientId(id);
        if (orders.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        for (Order order : orders) {
            Link selfLink = linkTo(methodOn(OrderController.class).getOrderById(order.getOrderId())).withSelfRel();
            Link itemsLink = linkTo(methodOn(OrderController.class).getOrderItemsByOrderId(order.getOrderId())).withRel("order-items");
            order.add(selfLink);
            order.add(itemsLink);
        }
        Link link = linkTo(methodOn(ClientController.class).getAllOrdersByClientId(id)).withSelfRel();
        return new ResponseEntity<>(CollectionModel.of(orders, link), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/reviews", produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<Review>> getAllReviewsByClientId(@PathVariable Long id) {
        List<Review> reviews = clientService.getAllReviewsByClientId(id);
        if (reviews.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        for (Review review : reviews) {
            Link selfLink = linkTo(methodOn(ReviewController.class).getReviewById(review.getReviewId())).withSelfRel();
            review.add(selfLink);
        }
        Link link = linkTo(methodOn(ClientController.class).getAllReviewsByClientId(id)).withSelfRel();
        return new ResponseEntity<>(CollectionModel.of(reviews, link), HttpStatus.OK);
    }

    private EntityModel<Client> generateLinks(Client client, Long id) {
        EntityModel<Client> entityModel = EntityModel.of(client);
        WebMvcLinkBuilder link = linkTo(methodOn(ClientController.class).getAllClients());
        entityModel.add(link.withRel("all-clients"));
        entityModel.add(linkTo(methodOn(ClientController.class).getClientById(id)).withSelfRel());
        entityModel.add(linkTo(methodOn(ClientController.class).getAllOrdersByClientId(id)).withRel("orders"));
        entityModel.add(linkTo(methodOn(ClientController.class).getAllReviewsByClientId(id)).withRel("reviews"));
        return entityModel;
    }
}