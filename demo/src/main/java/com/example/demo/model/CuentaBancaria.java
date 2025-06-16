package com.example.demo.model;
import com.example.demo.model.CuentaBancaria;

import jakarta.persistence.*;

@Entity
public class CuentaBancaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cedula;

    private Double saldo;

    // Getters y setters
}
