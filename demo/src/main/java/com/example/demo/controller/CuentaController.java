package com.example.demo.controller;

import com.example.demo.model.CuentaBancaria;
import com.example.demo.service.CuentaBancariaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CuentaController {

    private final CuentaBancariaService cuentaBancariaService;

    public CuentaController(CuentaBancariaService cuentaBancariaService) {
        this.cuentaBancariaService = cuentaBancariaService;
    }

    @PostMapping("/cuentas/crear")
    public String crearCuenta(@RequestParam("titular") String titular, Model model) {
        CuentaBancaria nuevaCuenta = new CuentaBancaria();
        nuevaCuenta.setTitular(titular);
        nuevaCuenta.setSaldo(0.0);
        cuentaBancariaService.crearCuenta(nuevaCuenta);

        model.addAttribute("mensaje", "Cuenta creada exitosamente con ID: " + nuevaCuenta.getId());
        return "crear-cuenta";
    }

    @PostMapping("/cuentas/consultar")
    public String consultarSaldo(@RequestParam("id") Long id, Model model) {
        CuentaBancaria cuenta = cuentaBancariaService.consultarSaldo(id)
                .orElse(null);

        if (cuenta == null) {
            model.addAttribute("error", "Cuenta no encontrada con ID: " + id);
        } else {
            model.addAttribute("cuenta", cuenta);
        }

        return "consultar-saldo";
    }

    @PostMapping("/cuentas/depositar")
    public String depositar(@RequestParam("id") Long id, @RequestParam("monto") Double monto, Model model) {
        try {
            CuentaBancaria cuenta = cuentaBancariaService.depositar(id, monto);
            model.addAttribute("mensaje", "Depósito exitoso. Nuevo saldo: " + cuenta.getSaldo());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "depositar";
    }

    @PostMapping("/cuentas/retirar")
    public String retirar(@RequestParam("id") Long id, @RequestParam("monto") Double monto, Model model) {
        try {
            CuentaBancaria cuenta = cuentaBancariaService.retirar(id, monto);
            model.addAttribute("mensaje", "Retiro exitoso. Nuevo saldo: " + cuenta.getSaldo());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "retirar";
    }
}