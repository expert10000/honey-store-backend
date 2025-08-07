package edu.project.store.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude // Prevents Lombok recursion in toString()
    @EqualsAndHashCode.Exclude // Prevents Lombok recursion
    @JsonBackReference
    private Product product;

    private String section;
    @Lob
    private String content;

    private LocalDateTime scrapedAt;
}
