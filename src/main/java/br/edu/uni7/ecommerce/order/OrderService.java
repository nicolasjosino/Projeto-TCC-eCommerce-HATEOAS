package br.edu.uni7.ecommerce.order;

import br.edu.uni7.ecommerce.client.Client;
import br.edu.uni7.ecommerce.client.ClientRepository;
import br.edu.uni7.ecommerce.orderitem.OrderItem;
import br.edu.uni7.ecommerce.orderitem.OrderItemRepository;
import br.edu.uni7.ecommerce.product.Product;
import br.edu.uni7.ecommerce.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    public Order saveOrder(OrderDTO orderDTO) {
        Client client = clientRepository.findById(orderDTO.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));

        Order order = new Order();
        order.setOrderDate(orderDTO.getOrderDate());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setClient(client);

        Order savedOrder = orderRepository.save(order);

        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            List<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(itemDTO -> {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDTO.getQuantity());
                return orderItem;
            }).collect(Collectors.toList());

            orderItemRepository.saveAll(orderItems);
            savedOrder.setOrderItems(orderItems);
        }

        return savedOrder;
    }


    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrderWithItems(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));

        Client client = clientRepository.findById(orderDTO.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));

        order.setOrderDate(orderDTO.getOrderDate());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setClient(client);

        List<OrderItem> existingOrderItems = orderItemRepository.findByOrderOrderId(id);
        orderItemRepository.deleteAll(existingOrderItems);

        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            List<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(itemDTO -> {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDTO.getQuantity());
                return orderItem;
            }).collect(Collectors.toList());

            orderItemRepository.saveAll(orderItems);
            order.setOrderItems(orderItems);
        }

        return orderRepository.save(order);
    }


    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderOrderId(orderId);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if (OrderStatus.valid(order.getOrderStatus(), newStatus)) {
            order.setOrderStatus(newStatus);
        } else {
            throw new OrderStatusInvalid(newStatus);
        }
        orderRepository.save(order);
        return order;
    }

}
