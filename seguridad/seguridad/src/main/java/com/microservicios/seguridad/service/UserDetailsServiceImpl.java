package com.microservicios.seguridad.service;

import com.microservicios.seguridad.model.Usuario;
import com.microservicios.seguridad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.microservicios.seguridad.model.Rol;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        System.out.println("🔍 Usuario encontrado en BD: " + usuario.getUsername());
        System.out.println("🔍 Contraseña en BD: " + usuario.getPassword());

        // ✅ Convertir los roles correctamente a String
        List<String> roles = usuario.getRoles().stream()
                .map(Rol::getNombre) // ✅ Obtener solo el nombre del rol
                .map(String::toUpperCase) // ✅ Convertirlo a mayúsculas
                .toList();

        System.out.println("✅ Roles convertidos: " + roles);

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(roles.toArray(new String[0])) // ✅ Asignar los roles correctamente
                .build();
    }
}