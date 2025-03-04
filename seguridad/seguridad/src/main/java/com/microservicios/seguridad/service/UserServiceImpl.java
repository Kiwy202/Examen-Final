package com.microservicios.seguridad.service;

import com.microservicios.seguridad.model.Usuario;
import com.microservicios.seguridad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioExistente.setUsername(usuario.getUsername());
        usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuarioExistente);
    }
}