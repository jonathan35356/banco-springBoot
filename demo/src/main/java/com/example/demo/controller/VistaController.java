package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping("/")
    public String loginPage() {
        return "login"; // Página de inicio con el formulario de login
    }

    @GetMapping("/index.html")
    public String indexPage() {
        return "index"; // Página principal que apunta a index.html
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/"; // Redirige al login después de cerrar sesión
    }
}
