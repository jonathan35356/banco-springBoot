package com.example.demo.controller;

import com.example.demo.model.CuentaBancaria;
import com.example.demo.service.CuentaBancariaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaBancariaController {

    private final CuentaBancariaService service;

    public CuentaBancariaController(CuentaBancariaService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaBancaria> consultarSaldo(@PathVariable Long id) {
        Optional<CuentaBancaria> cuenta = service.consultarSaldo(id); // Cambiar lógica según el servicio
        return cuenta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CuentaBancaria> crearCuenta(@RequestBody CuentaBancaria cuenta) {
        return ResponseEntity.ok(service.crearCuenta(cuenta));
    }
}
