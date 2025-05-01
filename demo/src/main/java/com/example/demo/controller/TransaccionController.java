package com.example.demo.controller;

import com.example.demo.model.Transaccion;
import com.example.demo.service.TransaccionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    // Redirige al formulario para ingresar el ID
    @GetMapping("/cuentas/historial/formulario")
    public String mostrarFormularioHistorial() {
        return "historial-formulario";
    }

    // Consulta el historial de transacciones
    @GetMapping("/cuentas/historial")
    public String verHistorial(@RequestParam("id") Long cuentaId, Model model) {
        List<Transaccion> transacciones = transaccionService.obtenerTransaccionesPorCuenta(cuentaId);
        model.addAttribute("transacciones", transacciones);
        return "historial";
    }
}