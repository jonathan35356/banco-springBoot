package com.example.demo.controller;

import com.example.demo.service.CuentaBancariaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransferenciaController {

    private final CuentaBancariaService cuentaBancariaService;

    public TransferenciaController(CuentaBancariaService cuentaBancariaService) {
        this.cuentaBancariaService = cuentaBancariaService;
    }

    // Maneja solicitudes GET para mostrar el formulario de transferencia
    @GetMapping("/cuentas/transferir")
    public String mostrarFormularioTransferencia() {
        return "transferir";
    }

    // Maneja solicitudes POST para realizar la transferencia
    @PostMapping("/cuentas/transferir")
    public String transferir(@RequestParam("origenId") Long origenId,
                             @RequestParam("destinoId") Long destinoId,
                             @RequestParam("monto") Double monto,
                             Model model) {
        try {
            cuentaBancariaService.transferir(origenId, destinoId, monto);
            model.addAttribute("mensaje", "Transferencia realizada con éxito");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "transferir";
    }
}
