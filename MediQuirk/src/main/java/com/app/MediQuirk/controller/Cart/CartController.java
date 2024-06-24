package com.app.MediQuirk.controller.Cart;

import com.app.MediQuirk.model.CartItem;
import com.app.MediQuirk.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController{

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity, Model model) {
        try {
            cartService.addToCart(productId, quantity);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/cart?error=" + e.getMessage();
        }
        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(Model model, @RequestParam(value = "error", required = false) String error) {
        List<CartItem> cartItems = cartService.getCartItems();
        BigDecimal totalPrice = cartService.getTotalPrice(); // Changed to BigDecimal

        if (cartItems.isEmpty()) {
            model.addAttribute("errorMessage", "Your cart is empty.");
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice); // Changed to BigDecimal

        if (error != null) {
            model.addAttribute("errorMessage", error);
        }

        return "cart/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeItemFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    @PostMapping("/increase")
    public String increaseQuantity(@RequestParam("productId") Long productId) {
        cartService.increaseQuantity(productId);
        return "redirect:/cart";
    }

    @PostMapping("/decrease")
    public String decreaseQuantity(@RequestParam("productId") Long productId) {
        cartService.decreaseQuantity(productId);
        return "redirect:/cart";
    }
    @PostMapping("/checkout")
    public String checkout(Model model) {
        return "cart/checkout"; // Đảm bảo rằng có tệp template cart/checkout.html
    }
}
