package com.example.demo.controller;

import com.example.demo.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam("cedula") String cedula,
                              @RequestParam("pin") String pin,
                              Model model) {
        if (usuarioService.autenticar(cedula, pin).isPresent()) {
            return new RedirectView("/index"); // Redirige al inicio
        } else {
            model.addAttribute("error", "Cédula o PIN incorrectos");
            return new RedirectView("/login"); // Redirige al login con mensaje de error
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam("nombre") String nombre,
                           @RequestParam("cedula") String cedula,
                           @RequestParam("pin") String pin,
                           Model model) {
        try {
            if (usuarioService.registrarUsuario(nombre, cedula, pin)) {
                model.addAttribute("mensaje", "Registro exitoso");
                return "login"; // Redirige al login
            } else {
                model.addAttribute("error", "La cédula ya está registrada");
                return "register";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}