package com.example.demo.controller;

import com.example.demo.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UsuarioService usuarioService;

    public RegisterController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/register")
    public String mostrarFormularioRegistro() {
        return "register"; // Nombre de la plantilla HTML
    }

    @PostMapping("/register")
    public String registrarUsuario(@RequestParam("nombre") String nombre,
                                   @RequestParam("cedula") String cedula,
                                   @RequestParam("pin") String pin,
                                   Model model) {
        try {
            boolean registrado = usuarioService.registrarUsuario(nombre, cedula, pin);
            if (registrado) {
                model.addAttribute("mensaje", "Usuario registrado exitosamente.");
                return "login"; // Redirige al login después del registro
            } else {
                model.addAttribute("error", "La cédula ya está registrada.");
                return "register"; // Permanece en la página de registro
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Permanece en la página de registro
        }
    }
}