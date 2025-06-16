package com.example.demo.controller;

import com.example.demo.model.CuentaBancaria;
import com.example.demo.service.CuentaBancariaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.model.Usuario;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import com.example.demo.service.UsuarioService;

@Controller
public class CuentaController {

    private final CuentaBancariaService cuentaBancariaService;
    private final UsuarioService usuarioService; // Agregar UsuarioService

    public CuentaController(CuentaBancariaService cuentaBancariaService, UsuarioService usuarioService) {
        this.cuentaBancariaService = cuentaBancariaService;
        this.usuarioService = usuarioService; // Inicializar UsuarioService
    }

    @GetMapping("/cuentas/depositar")
    public String mostrarFormularioDeposito(Model model) {
        model.addAttribute("mensaje", "");
        return "depositar"; // Nombre de la plantilla HTML
    }

    @PostMapping("/cuentas/depositar")
    public String depositar(@RequestParam("monto") Double monto, Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("error", "Debe iniciar sesión para realizar un depósito.");
            return "login"; // Redirige a la página de inicio de sesión
        }

        try {
            String cedula = principal.getName(); // Obtener la cédula del usuario autenticado
            Usuario usuario = usuarioService.depositar(cedula, monto);

            model.addAttribute("mensaje", "Depósito realizado con éxito. Nuevo saldo: $" + usuario.getSaldo());
            model.addAttribute("titular", usuario.getNombre());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "depositar";
    }
}