package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> autenticar(String cedula, String pin) {
        return usuarioRepository.findByCedula(cedula)
                .filter(usuario -> usuario.getPin().equals(pin));
    }

    public boolean registrarUsuario(String nombre, String cedula, String pin) {
        if (usuarioRepository.findByCedula(cedula).isPresent()) {
            return false; // La cédula ya está registrada
        }
        if (pin.length() != 4) {
            throw new IllegalArgumentException("El PIN debe tener exactamente 4 dígitos.");
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setCedula(cedula);
        nuevoUsuario.setPin(pin);
        usuarioRepository.save(nuevoUsuario);
        return true;
    }
}