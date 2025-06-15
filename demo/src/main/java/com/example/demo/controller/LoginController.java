package com.example.demo.controller;

import com.example.demo.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public String login(@RequestParam("cedula") String cedula,
                        @RequestParam("pin") String pin,
                        Model model) {
        if (usuarioService.autenticar(cedula, pin).isPresent()) {
            return "redirect:/index.html"; // Redirige directamente a index.html
        } else {
            model.addAttribute("error", "Cédula o PIN incorrectos");
            return "login"; // Permanece en la página de login con un mensaje de error
        }
    }
}