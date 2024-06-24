package com.app.MediQuirk.services;

import com.app.MediQuirk.model.CartItem;
import com.app.MediQuirk.model.Product;
import com.app.MediQuirk.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService{

    private List<CartItem> cartItems = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        // Kiểm tra số lượng sản phẩm có sẵn
        if (quantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds stock available.");
        }

        CartItem existingCartItem = cartItems.stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantity;
            if (newQuantity > product.getStockQuantity()) {
                throw new IllegalArgumentException("Quantity exceeds stock available.");
            }
            existingCartItem.setQuantity(newQuantity);
        } else {
            cartItems.add(new CartItem(product, quantity));
        }
    }

    public void increaseQuantity(Long productId) {
        CartItem cartItem = cartItems.stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not in cart: " + productId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        if (cartItem.getQuantity() + 1 > product.getStockQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds stock available.");
        }

        cartItem.setQuantity(cartItem.getQuantity() + 1);
    }

    public void decreaseQuantity(Long productId) {
        CartItem cartItem = cartItems.stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not in cart: " + productId));

        if (cartItem.getQuantity() - 1 < 1) {
            throw new IllegalArgumentException("Quantity cannot be less than 1.");
        }

        cartItem.setQuantity(cartItem.getQuantity() - 1);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getProductId().equals(productId));
    }

    public void clearCart() {
        cartItems.clear();
    }

    public BigDecimal getTotalPrice() {
        if (cartItems == null || cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return cartItems.stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getPrice() != null)
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
