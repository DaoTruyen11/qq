package com.app.MediQuirk.controller.Admin;

import com.app.MediQuirk.model.Supplier;
import com.app.MediQuirk.services.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SupplierController {

    @Autowired
    private final SupplierService supplierService;

    // GET request to show add supplier form
    @GetMapping("/suppliers/add")
    public String showAddForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "Admin/suppliers/add-supplier";
    }

    // POST request to add a new supplier
    @PostMapping("/suppliers/add")
    public String addSupplier(@Valid Supplier supplier, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Admin/suppliers/add-supplier";
        }
        supplierService.addSupplier(supplier);
        return "redirect:/suppliers";
    }

    // GET request to show list of suppliers
    @GetMapping("/suppliers")
    public String listSuppliers(Model model) {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        return "Admin/suppliers/supplier-list";
    }

    // GET request to show supplier edit form
    @GetMapping("/suppliers/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Supplier supplier = supplierService.getSupplierById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier Id: " + id));
        model.addAttribute("supplier", supplier);
        return "Admin/suppliers/update-supplier";
    }

    // POST request to update supplier
    @PostMapping("/suppliers/update/{id}")
    public String updateSupplier(@PathVariable("id") Long id, @Valid Supplier supplier, BindingResult result, Model model) {
        if (result.hasErrors()) {
            supplier.setSupplierId(id); // Ensure the ID is set before returning the form with errors
            return "Admin/suppliers/update-supplier";
        }
        supplier.setSupplierId(id); // Ensure the ID is set before updating
        supplierService.updateSupplier(supplier);
        return "redirect:/suppliers";
    }

    // GET request for deleting supplier
    @GetMapping("/suppliers/delete/{id}")
    public String deleteSupplier(@PathVariable("id") Long id, Model model) {
        Supplier supplier = supplierService.getSupplierById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier Id: " + id));
        supplierService.deleteSupplierById(id);
        return "redirect:/suppliers";
    }
}
