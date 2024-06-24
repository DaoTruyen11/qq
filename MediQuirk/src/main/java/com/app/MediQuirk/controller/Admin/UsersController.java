package com.app.MediQuirk.controller.Admin;

import com.app.MediQuirk.model.Role;
import com.app.MediQuirk.model.Users;
import com.app.MediQuirk.services.RoleService;
import com.app.MediQuirk.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String getAllUsers(Model model) {
        List<Users> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        return "Admin/users/list";
    }

    @GetMapping("/add")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new Users());
        model.addAttribute("roles", roleService.getAllRoles());
        return "Admin/users/create";
    }

    @PostMapping("/add")
    public String createUser(@ModelAttribute("user") Users user, @RequestParam List<Long> roleIds, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "Admin/users/create";
        }
        user.setRoles(roleService.getRolesByIds(roleIds));
        usersService.addUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Users user = usersService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "Admin/users/update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") Users user, @RequestParam List<Long> roleIds, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            user.setUser_id(id);
            return "Admin/users/update";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/update";
        }
        user.setRoles(roleService.getRolesByIds(roleIds));
        usersService.updateUser(id, user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        usersService.deleteUser(id);
        return "redirect:/users";
    }
}
