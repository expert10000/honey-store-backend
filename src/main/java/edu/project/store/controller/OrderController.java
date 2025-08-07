package edu.project.store.controller;

import edu.project.store.dto.OrderRequest;
import edu.project.store.dto.OrderStatusUpdateRequest;
import edu.project.store.model.Order;
import edu.project.store.model.OrderItem;
import edu.project.store.model.OrderStatus;
import edu.project.store.model.Product;
import edu.project.store.repository.OrderRepository;
import edu.project.store.repository.ProductRepository;
import edu.project.store.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository,
            ProductRepository productRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
/*
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatusEx(
            @PathVariable Long id,
            @RequestBody OrderStatusUpdateRequest req) {
        OrderStatus newStatus = req.getStatus();
        if (newStatus == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public Order placeOrder(@RequestBody OrderRequest orderRequest, Principal principal) {
        Order order = new Order();
        order.setUsername(principal != null ? principal.getName() : "guest");
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setDeliveryMethod(orderRequest.deliveryMethod);
        order.setDeliveryAddress(orderRequest.deliveryAddress);
        order.setDeliveryComment(orderRequest.deliveryComment);
        order.setInvoice(orderRequest.invoice);

        // Map items
        List<OrderItem> items = orderRequest.cart.stream().map(reqItem -> {
            OrderItem item = new OrderItem();
            Product product = productRepository.findById(reqItem.productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + reqItem.productId));
            item.setProduct(product); // sets product_id
            item.setUnitPrice(product.getPrice()); // sets unit_price
            item.setProductName(product.getName());
            item.setQuantity(reqItem.quantity);
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);

        return orderRepository.save(order);
    }

    // Get logged-in user's orders
    @GetMapping("/my")
    public List<Order> getMyOrders(Principal principal) {
        String username = principal != null ? principal.getName() : "guest";

        return orderService.getOrdersForUser(username);
    }

    @PutMapping("/{orderId}/status")
    public Order updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest req) {
        OrderStatus newStatus = req.getStatus();
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null or invalid");
        }
        return orderService.updateOrderStatus(orderId, newStatus);
    }

}
