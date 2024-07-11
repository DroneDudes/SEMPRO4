package org.dronedudes.backend.Product;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(String name, String description){
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        return productRepository.save(product);
    }
}
