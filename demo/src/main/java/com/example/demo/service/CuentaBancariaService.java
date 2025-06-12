package com.example.demo.service;

import com.example.demo.model.CuentaBancaria;
import com.example.demo.model.Transaccion;
import com.example.demo.repository.CuentaBancariaRepository;
import com.example.demo.repository.TransaccionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
            throw new RuntimeException("Saldo insuficiente para realizar el retiro");
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

    public void transferir(Long origenId, Long destinoId, Double monto) {
        CuentaBancaria origen = repository.findById(origenId)
                .orElseThrow(() -> new RuntimeException("Cuenta de origen no encontrada"));
        CuentaBancaria destino = repository.findById(destinoId)
                .orElseThrow(() -> new RuntimeException("Cuenta de destino no encontrada"));

        if (origen.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente en la cuenta de origen");
        }

        origen.setSaldo(origen.getSaldo() - monto);
        destino.setSaldo(destino.getSaldo() + monto);

        repository.save(origen);
        repository.save(destino);

        // Registrar transacciones
        Transaccion transaccionOrigen = new Transaccion();
        transaccionOrigen.setCuenta(origen);
        transaccionOrigen.setTipo("TRANSFERENCIA SALIENTE");
        transaccionOrigen.setMonto(monto);
        transaccionOrigen.setFecha(LocalDateTime.now());
        transaccionRepository.save(transaccionOrigen);

        Transaccion transaccionDestino = new Transaccion();
        transaccionDestino.setCuenta(destino);
        transaccionDestino.setTipo("TRANSFERENCIA ENTRANTE");
        transaccionDestino.setMonto(monto);
        transaccionDestino.setFecha(LocalDateTime.now());
        transaccionRepository.save(transaccionDestino);
    }

    public List<CuentaBancaria> obtenerTodasLasCuentas() {
        return repository.findAll();
    }

    public CuentaBancaria depositarPorCedula(String cedula, Double monto) {
        CuentaBancaria cuenta = repository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con la cédula: " + cedula));
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

    public List<CuentaBancaria> consultarSaldoPorCedula(String cedula) {
        return repository.findByCedula(cedula)
                .map(List::of)
                .orElseThrow(() -> new RuntimeException("No se encontraron cuentas con la cédula: " + cedula));
    }

    public CuentaBancaria retirarPorCedula(String cedula, Double monto) {
        CuentaBancaria cuenta = repository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con la cédula: " + cedula));

        if (cuenta.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente para realizar el retiro");
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

    public void transferirPorCedula(String cedulaOrigen, String cedulaDestino, Double monto) {
        CuentaBancaria cuentaOrigen = repository.findByCedula(cedulaOrigen)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada con la cédula: " + cedulaOrigen));

        CuentaBancaria cuentaDestino = repository.findByCedula(cedulaDestino)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada con la cédula: " + cedulaDestino));

        if (cuentaOrigen.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente en la cuenta origen para realizar la transferencia");
        }

        // Actualizar saldos
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
        cuentaDestino.setSaldo(cuentaDestino.getSaldo() + monto);

        // Guardar cambios en la base de datos
        repository.save(cuentaOrigen);
        repository.save(cuentaDestino);

        // Registrar la transacción
        Transaccion transaccionOrigen = new Transaccion();
        transaccionOrigen.setCuenta(cuentaOrigen);
        transaccionOrigen.setTipo("TRANSFERENCIA SALIDA");
        transaccionOrigen.setMonto(monto);
        transaccionOrigen.setFecha(LocalDateTime.now());
        transaccionRepository.save(transaccionOrigen);

        Transaccion transaccionDestino = new Transaccion();
        transaccionDestino.setCuenta(cuentaDestino);
        transaccionDestino.setTipo("TRANSFERENCIA ENTRADA");
        transaccionDestino.setMonto(monto);
        transaccionDestino.setFecha(LocalDateTime.now());
        transaccionRepository.save(transaccionDestino);
    }

    public List<Transaccion> obtenerHistorialPorCedula(String cedula) {
        CuentaBancaria cuenta = repository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con la cédula: " + cedula));
        return transaccionRepository.findByCuenta(cuenta);
    }
}
