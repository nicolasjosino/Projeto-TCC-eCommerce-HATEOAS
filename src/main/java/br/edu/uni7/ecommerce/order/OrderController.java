package br.edu.uni7.ecommerce.order;

import br.edu.uni7.ecommerce.orderitem.OrderItem;
import br.edu.uni7.ecommerce.orderitem.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        Order newOrder = orderService.saveOrder(orderDTO);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        Order updatedOrder = orderService.updateOrderWithItems(id, orderDTO);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrderId(@PathVariable Long id) {
        List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(id);
        if (orderItems != null && !orderItems.isEmpty()) {
            return new ResponseEntity<>(orderItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Order> payOrder(@PathVariable Long id) {
        Order paidOrder = orderService.updateOrderStatus(id, OrderStatus.PAID);
        return new ResponseEntity<>(paidOrder, HttpStatus.OK);
    }

    @PostMapping("/{id}/fulfill")
    public ResponseEntity<Order> fulfillOrder(@PathVariable Long id) {
        Order paidOrder = orderService.updateOrderStatus(id, OrderStatus.FULFILLED);
        return new ResponseEntity<>(paidOrder, HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        Order paidOrder = orderService.updateOrderStatus(id, OrderStatus.CANCELLED);
        return new ResponseEntity<>(paidOrder, HttpStatus.OK);
    }
}
