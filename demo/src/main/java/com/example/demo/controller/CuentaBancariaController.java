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

    @PostMapping
    public ResponseEntity<CuentaBancaria> crearCuenta(@RequestBody CuentaBancaria cuenta) {
        return ResponseEntity.ok(service.crearCuenta(cuenta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaBancaria> consultarSaldo(@PathVariable Long id) {
        Optional<CuentaBancaria> cuenta = service.consultarSaldo(id);
        return cuenta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/depositar")
    public ResponseEntity<CuentaBancaria> depositar(@PathVariable Long id, @RequestParam Double monto) {
        return ResponseEntity.ok(service.depositar(id, monto));
    }

    @PostMapping("/{id}/retirar")
    public ResponseEntity<CuentaBancaria> retirar(@PathVariable Long id, @RequestParam Double monto) {
        return ResponseEntity.ok(service.retirar(id, monto));
    }
}
