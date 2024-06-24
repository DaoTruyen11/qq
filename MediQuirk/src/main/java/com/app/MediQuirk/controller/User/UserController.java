package com.app.MediQuirk.controller.User;

import com.app.MediQuirk.model.Users;
import com.app.MediQuirk.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "User/users/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new Users());
        return "User/users/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") Users user,
                           @NotNull BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "User/users/register";
        }

        userService.save(user);
        userService.setDefaultRole(user.getUsername(), false);

        return "redirect:/login";
    }
    @GetMapping("/oauth2/success")
    public String oauth2LoginSuccess(@AuthenticationPrincipal OAuth2User principal, Model model) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");

        Users user = userService.processOAuth2User(email, name);

        model.addAttribute("user", user);
        return "redirect:/";
    }
}