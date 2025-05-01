package com.example.demo.service;

import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.Transaccion;
import com.example.demo.repository.CuentaBancariaRepository;
import com.example.demo.repository.TransaccionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CuentaBancariaService {

    private final CuentaBancariaRepository repository;
    private final TransaccionRepository transaccionRepository;

    public CuentaBancariaService(CuentaBancariaRepository repository, TransaccionRepository transaccionRepository) {
        this.repository = repository;
        this.transaccionRepository = transaccionRepository;
    }

    public CuentaBancaria crearCuenta(CuentaBancaria cuenta) {
        cuenta.setSaldo(0.0); // Inicializa el saldo en 0
        return repository.save(cuenta);
    }

    public Optional<CuentaBancaria> consultarSaldo(Long id) {
        return repository.findById(id);
    }

    public CuentaBancaria depositar(Long id, Double monto) {
        CuentaBancaria cuenta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setSaldo(cuenta.getSaldo() + monto);
        repository.save(cuenta);

        // Registrar la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setCuenta(cuenta);
        transaccion.setTipo("DEPOSITO");
        transaccion.setMonto(monto);
        transaccion.setFecha(LocalDateTime.now());
        transaccionRepository.save(transaccion);

        return cuenta;
    }

    public CuentaBancaria retirar(Long id, Double monto) {
        CuentaBancaria cuenta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        if (cuenta.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente");
        }
        cuenta.setSaldo(cuenta.getSaldo() - monto);
        repository.save(cuenta);

        // Registrar la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setCuenta(cuenta);
        transaccion.setTipo("RETIRO");
        transaccion.setMonto(monto);
        transaccion.setFecha(LocalDateTime.now());
        transaccionRepository.save(transaccion);

        return cuenta;
    }
}
