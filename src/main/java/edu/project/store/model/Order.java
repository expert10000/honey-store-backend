package edu.project.store.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders") // avoid SQL reserved keyword conflict
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appwriteUserId;

    private String username; // Optional: if you want to store username directly

    // Was orderDate; for clarity and consistency with code, use createdAt
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // --- Add missing fields below ---
    private String deliveryMethod;
    private String deliveryAddress;
    private String deliveryComment;
    private Boolean invoice;

    // Optionally: more fields (insurance, phone, etc)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
}
