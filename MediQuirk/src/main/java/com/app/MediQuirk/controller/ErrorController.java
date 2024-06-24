package com.app.MediQuirk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ErrorController {
    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}
