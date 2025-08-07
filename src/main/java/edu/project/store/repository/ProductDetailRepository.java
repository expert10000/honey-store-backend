package edu.project.store.repository;


import edu.project.store.model.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<OrderItem, Long> 
 {
     List<ProductDetail> findByProductId(Long productId);
}
