package com.app.MediQuirk.controller.Admin;

import com.app.MediQuirk.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
    @Autowired
    private ProductService productService;
    @RequestMapping("/admin")
    public String Admin(){

        return "Admin/index";
    }
    @GetMapping("/admin/products")
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "Admin/product";
    }

}
