package com.example.demo.service;

import com.example.demo.model.Transaccion;
import com.example.demo.repository.TransaccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(Long cuentaId) {
        return transaccionRepository.findByCuentaId(cuentaId);
    }
}
