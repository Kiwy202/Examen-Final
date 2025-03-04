package com.microservicios.seguridad.service;

import com.microservicios.seguridad.dto.AuthResponse;
import com.microservicios.seguridad.dto.LoginRequest;
import com.microservicios.seguridad.dto.RegisterRequest;
import com.microservicios.seguridad.model.Rol;
import com.microservicios.seguridad.model.Usuario;
import com.microservicios.seguridad.repository.RolRepository;
import com.microservicios.seguridad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        // 🔍 Buscar el rol que se pasa en el request, si no existe, lanza error
        Rol rol = rolRepository.findByNombre(request.getRol())
                .orElseThrow(() -> new RuntimeException("Role " + request.getRol() + " not found"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);  // ✅ Asignamos el rol correcto desde la solicitud

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token);
    }

    public boolean validateToken(String token) {
        Usuario usuario = usuarioRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new RuntimeException("User not found"));
        return jwtService.isTokenValid(token, usuario);
    }
}