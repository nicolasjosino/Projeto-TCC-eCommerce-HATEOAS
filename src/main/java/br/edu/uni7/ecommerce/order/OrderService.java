package br.edu.uni7.ecommerce.order;

import br.edu.uni7.ecommerce.orderitem.OrderItem;
import br.edu.uni7.ecommerce.orderitem.OrderItemRepository;
import br.edu.uni7.ecommerce.orderitem.OrderItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order saveOrderWithItems(Order order) {
        Order newOrder = orderRepository.save(order);
        if (!order.getOrderItems().isEmpty()) {
            for (OrderItem item : order.getOrderItems()) {
                orderItemRepository.save(item);
            }
        }
        return newOrder;
    }
}
