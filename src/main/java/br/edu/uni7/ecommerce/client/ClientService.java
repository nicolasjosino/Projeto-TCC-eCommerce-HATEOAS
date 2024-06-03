package br.edu.uni7.ecommerce.client;

import br.edu.uni7.ecommerce.order.Order;
import br.edu.uni7.ecommerce.order.OrderRepository;
import br.edu.uni7.ecommerce.review.Review;
import br.edu.uni7.ecommerce.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public List<Order> getAllOrdersByClientId(Long clientId) {
        return orderRepository.findByClientClientId(clientId);
    }

    public List<Review> getAllReviewsByClientId(Long clientId) {
        return reviewRepository.findByClientClientId(clientId);
    }
}
