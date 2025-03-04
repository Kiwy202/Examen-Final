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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        Rol rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRoles(Set.of(rolUser));

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(Map.of("roles", usuario.getAuthorities()), usuario);

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(Map.of("roles", userDetails.getAuthorities()), userDetails);

        return new AuthResponse(token);
    }
}