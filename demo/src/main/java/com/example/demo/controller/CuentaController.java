package com.example.demo.controller;

import com.example.demo.model.CuentaBancaria;
import com.example.demo.service.CuentaBancariaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class CuentaController {

    private final CuentaBancariaService cuentaBancariaService;

    public CuentaController(CuentaBancariaService cuentaBancariaService) {
        this.cuentaBancariaService = cuentaBancariaService;
    }

    @PostMapping("/cuentas/crear")
    public String crearCuenta(@RequestParam("titular") String titular,
                              @RequestParam("cedula") String cedula,
                              @RequestParam("pin") String pin,
                              Model model) {
        if (pin.length() != 4) {
            model.addAttribute("error", "El PIN debe tener exactamente 4 dígitos.");
            return "crear-cuenta";
        }

        CuentaBancaria nuevaCuenta = new CuentaBancaria();
        nuevaCuenta.setTitular(titular);
        nuevaCuenta.setCedula(cedula);
        nuevaCuenta.setPin(pin);
        nuevaCuenta.setSaldo(0.0);
        cuentaBancariaService.crearCuenta(nuevaCuenta);

        model.addAttribute("mensaje", "Cuenta creada exitosamente con ID: " + nuevaCuenta.getId());
        return "crear-cuenta";
    }

    @PostMapping("/cuentas/consultar")
    public String consultarSaldo(@RequestParam("id") Long id, Model model) {
        Optional<CuentaBancaria> cuenta = cuentaBancariaService.consultarSaldo(id);

        if (cuenta.isEmpty()) {
            model.addAttribute("error", "Cuenta no encontrada con ID: " + id);
        } else {
            model.addAttribute("cuenta", cuenta.get());
        }

        return "consultar-saldo";
    }
}