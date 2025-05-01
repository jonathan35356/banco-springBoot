package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/cuentas/nueva")
    public String nuevaCuenta() {
        return "crear-cuenta";
    }

    @GetMapping("/cuentas/consultar")
    public String consultarSaldo() {
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
