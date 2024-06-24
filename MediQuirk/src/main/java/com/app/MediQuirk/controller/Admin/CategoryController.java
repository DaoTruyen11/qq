package com.app.MediQuirk.controller.Admin;

import com.app.MediQuirk.model.Category;
import com.app.MediQuirk.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    // GET request to show add category form
    @GetMapping("/categories/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "Admin/categories/add-category";
    }

    // POST request to add a new category
    @PostMapping("/categories/add")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Admin/categories/add-category";
        }
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    // GET request to show list of categories
    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "Admin/categories/category-list";
    }

    // GET request to show category edit form
    @GetMapping("/categories/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + id));
        model.addAttribute("category", category);
        return "Admin/categories/update-category";
    }

    // POST request to update category
    @PostMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, @Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            category.setCategoryId(id); // Ensure the ID is set before returning the form with errors
            return "Admin/categories/update-category";
        }
        category.setCategoryId(id); // Ensure the ID is set before updating
        categoryService.updateCategory(category);
        return "redirect:/categories";
    }

    // GET request for deleting category
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + id));
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }
}