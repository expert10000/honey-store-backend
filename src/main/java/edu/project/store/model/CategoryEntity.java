package edu.project.store.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


}
