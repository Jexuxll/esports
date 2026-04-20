package com.proyecto.esports.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/admin/login")
    public String mostrarLoginAdmin() {
        return "admin_login";
    }
}
