package edu.project.store.controller;

import edu.project.store.model.Product;
import edu.project.store.model.ProductDetail;
import edu.project.store.repository.ProductDetailRepository;
import edu.project.store.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000") // Adjust for your frontend URL
public class ProductController {
    private final ProductService productService;

    private final ProductDetailRepository productDetailRepository;

    public ProductController(ProductService productService, ProductDetailRepository productDetailRepository) {
        this.productService = productService;
        this.productDetailRepository = productDetailRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/details")
    public List<ProductDetail> getProductDetails(@PathVariable Long id) {
        return productDetailRepository.findByProductId(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id, @RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
