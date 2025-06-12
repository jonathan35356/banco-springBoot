package com.example.demo.controller;

import com.example.demo.model.CuentaBancaria;
import com.example.demo.service.CuentaBancariaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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
    public String consultarSaldo(@RequestParam("cedula") String cedula, Model model) {
        List<CuentaBancaria> cuentas = cuentaBancariaService.consultarSaldoPorCedula(cedula);

        if (cuentas.isEmpty()) {
            model.addAttribute("error", "No se encontraron cuentas con la cédula: " + cedula);
        } else {
            model.addAttribute("cuentas", cuentas);
        }

        return "consultar-saldo";
    }

    @PostMapping("/cuentas/depositar")
    public String depositar(@RequestParam("cedula") String cedula,
                            @RequestParam("monto") Double monto,
                            Model model) {
        try {
            CuentaBancaria cuenta = cuentaBancariaService.depositarPorCedula(cedula, monto);
            model.addAttribute("mensaje", "Depósito realizado con éxito. Nuevo saldo: $" + cuenta.getSaldo());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "depositar";
    }

    @PostMapping("/cuentas/retirar")
    public String retirar(@RequestParam("cedula") String cedula,
                          @RequestParam("monto") Double monto,
                          Model model) {
        try {
            CuentaBancaria cuenta = cuentaBancariaService.retirarPorCedula(cedula, monto);
            model.addAttribute("mensaje", "Retiro realizado con éxito. Nuevo saldo: $" + cuenta.getSaldo());
            model.addAttribute("titular", cuenta.getTitular());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "retirar";
    }
}