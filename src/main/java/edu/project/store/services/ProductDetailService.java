package edu.project.store.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.project.store.model.ProductDetail;
import edu.project.store.repository.ProductDetailRepository;

@Service
public class ProductDetailService {
    private final ProductDetailRepository repository;

    @Autowired
    public ProductDetailService(ProductDetailRepository repository) {
        this.repository = repository;
    }

    public List<ProductDetail> getDetailsForProduct(Long productId) {
        return repository.findByProductId(productId);
    }
}