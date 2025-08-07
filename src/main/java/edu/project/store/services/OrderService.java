package edu.project.store.services;

import edu.project.store.model.Order;
import edu.project.store.model.OrderItem;
import edu.project.store.model.OrderStatus;
import edu.project.store.model.Product;
import edu.project.store.repository.OrderRepository;
import edu.project.store.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.project.store.dto.OrderRequest;
import edu.project.store.dto.OrderRequest.OrderItemRequest;

@Service
public class OrderService {
      private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest, String username) {
        Order order = new Order();
        //order.setUsername(username);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);
        order.setDeliveryMethod(orderRequest.deliveryMethod);
        order.setDeliveryAddress(orderRequest.deliveryAddress);
        order.setDeliveryComment(orderRequest.deliveryComment);
        order.setInvoice(orderRequest.invoice);

        // Map items from DTO, validate product existence and price
        List<OrderItem> items = orderRequest.cart.stream().map(reqItem -> {
            Product product = productRepository.findById(reqItem.productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + reqItem.productId));
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductName(product.getName());
           item.setUnitPrice(product.getPrice()); // Always take price from DB
            item.setQuantity(reqItem.quantity);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
/* 
    public Order updateOrderStatus2(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
    */
public Order updateOrderStatus(Long orderId, OrderStatus status) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    order.setStatus(status);
    return orderRepository.save(order);
}

    public List<Order> getOrdersForUser(String username) {
        return orderRepository.findByUsername(username);
    }
}
