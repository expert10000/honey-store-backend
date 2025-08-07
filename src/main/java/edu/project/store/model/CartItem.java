package edu.project.store.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // Many cart items can refer to the same product
    private Product product;

    private Integer quantity;

}
