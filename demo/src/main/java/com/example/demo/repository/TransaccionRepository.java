package com.example.demo.repository;

import com.example.demo.model.Transaccion;
import com.example.demo.model.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByCuenta(CuentaBancaria cuenta);

    // Nuevo método para buscar transacciones por ID de cuenta
    List<Transaccion> findByCuentaId(Long cuentaId);
}
