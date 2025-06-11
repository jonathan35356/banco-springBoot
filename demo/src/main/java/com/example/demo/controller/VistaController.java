package com.example.demo.controller;

import com.example.demo.service.CuentaBancariaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.model.CuentaBancaria;
import java.util.Optional;

@Controller
public class VistaController {

    private final CuentaBancariaService cuentaBancariaService;

    // Constructor para inyectar el servicio
    public VistaController(CuentaBancariaService cuentaBancariaService) {
        this.cuentaBancariaService = cuentaBancariaService;
    }

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/cuentas/nueva")
    public String nuevaCuenta() {
        return "crear-cuenta";
    }

    @GetMapping("/cuentas/consultar")
    public String consultarSaldo(@RequestParam(value = "id", required = false) Long id, Model model) {
        model.addAttribute("cuentas", cuentaBancariaService.obtenerTodasLasCuentas());
        if (id != null) {
            try {
                Optional<CuentaBancaria> cuentaBancaria = cuentaBancariaService.consultarSaldo(id);
                if (cuentaBancaria.isPresent()) {
                    double saldo = cuentaBancaria.get().getSaldo(); // Suponiendo que CuentaBancaria tiene un método getSaldo()
                    model.addAttribute("saldo", saldo);
                } else {
                    model.addAttribute("error", "La cuenta bancaria no fue encontrada.");
                }
            } catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "consultar-saldo";
    }

    @GetMapping("/cuentas/depositar")
    public String depositar() {
        return "depositar";
    }

    @GetMapping("/cuentas/retirar")
    public String retirar() {
        return "retirar";
    }
}
