package com.app.MediQuirk.controller.Admin;

import com.app.MediQuirk.model.Product;
import com.app.MediQuirk.services.CategoryService;
import com.app.MediQuirk.services.CartService;
import com.app.MediQuirk.services.ProductService;
import com.app.MediQuirk.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CartService cartService;


    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/medicine";

    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "Admin/products/test";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "Admin/products/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute Product product,
                             BindingResult result,
                             @RequestParam("file") MultipartFile file,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            return "Admin/products/add-product";
        }

        try {
            if (!file.isEmpty()) {
                String fileName = saveFile(file);
                product.setImageUrl(fileName);
            }

            productService.addProduct(product);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Có lỗi xảy ra trong quá trình lưu tệp.");
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            return "Admin/products/add-product";
        }

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "Admin/products/update-product";
    }
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute Product product,
                                BindingResult result,
                                @RequestParam("file") MultipartFile file,
                                Model model) {
        if (result.hasErrors()) {
            product.setProductId(id);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            return "Admin/products/update-product";
        }

        try {
            Product existingProduct = productService.getProductById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

            if (!file.isEmpty()) {
                String FileName = saveFile(file);
                existingProduct.setImageUrl(FileName);
            }

            existingProduct.setProductName(product.getProductName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setSupplier(product.getSupplier());
            existingProduct.setStockQuantity(product.getStockQuantity());
            existingProduct.setPrescriptionRequired(product.isPrescriptionRequired());

            productService.updateProduct(existingProduct);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/products";
    }
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            return "Admin/products/update-product";
        }
        product.setProductId(id);
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadDir = Paths.get(UPLOADED_FOLDER);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path filePath = uploadDir.resolve(fileName);
        Files.write(filePath, file.getBytes());
        return fileName;
    }
    // View cart
    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "/cart/cart";
    }

    // Add product to cart
    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam int quantity) {
        cartService.addToCart(id, quantity);
        return "redirect:/cart/cart";
    }

    // Increase quantity
    @PostMapping("/cart/increase/{id}")
    public String increaseQuantity(@PathVariable Long id) {
        cartService.increaseQuantity(id);
        return "redirect:/cart/cart";
    }

    // Decrease quantity
    @PostMapping("/cart/decrease/{id}")
    public String decreaseQuantity(@PathVariable Long id) {
        cartService.decreaseQuantity(id);
        return "redirect:/cart/cart";
    }

    // Remove from cart
    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return "redirect:/cart/cart";
    }

    // Clear cart
    @PostMapping("/cart/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart/cart";
    }
}