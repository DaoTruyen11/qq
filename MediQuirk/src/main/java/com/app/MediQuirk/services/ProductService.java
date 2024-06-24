package com.app.MediQuirk.services;

import com.app.MediQuirk.model.Product;
import com.app.MediQuirk.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // Retrieve all products from the database
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Retrieve a product by its id
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Add a new product to the database
    public Product addProduct(@NotNull Product product) {
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(@NotNull Product product) {
        Long productId = product.getProductId();
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        Optional<Product> optionalProduct = getProductById(productId);
        if (optionalProduct.isEmpty()) {
            throw new IllegalStateException("Product with ID " + productId + " does not exist.");
        }

        Product existingProduct = optionalProduct.get();

        // Update only non-null fields
        if (product.getProductName() != null) {
            existingProduct.setProductName(product.getProductName());
        }
        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getCategory() != null) {
            existingProduct.setCategory(product.getCategory());
        }
        if (product.getImageUrl() != null) {
            existingProduct.setImageUrl(product.getImageUrl());
        }
        existingProduct.setPrescriptionRequired(product.isPrescriptionRequired());
        existingProduct.setStockQuantity(product.getStockQuantity());
        if (product.getSupplier() != null) {
            existingProduct.setSupplier(product.getSupplier());
        }

        return productRepository.save(existingProduct);
    }

    // Delete a product by its id
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }
}