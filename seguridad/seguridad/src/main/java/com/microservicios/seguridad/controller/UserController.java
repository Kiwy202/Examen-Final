package com.microservicios.seguridad.controller;

import com.microservicios.seguridad.model.Usuario;
import com.microservicios.seguridad.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Usuario>> getUsers() {
        return ResponseEntity.ok(userService.obtenerUsuariosPorRol("USER"));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> getAdmins() {
        return ResponseEntity.ok(userService.obtenerUsuariosPorRol("ADMIN"));
    }

    @PutMapping("/users/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario updatedUser, Authentication authentication) {
        log.info("Usuario autenticado: {}", authentication.getName());
        log.info("Roles: {}", authentication.getAuthorities());

        Usuario usuario = userService.actualizarUsuario(id, updatedUser);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/users/admin/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Usuario> actualizarAdmin(@PathVariable Long id, @RequestBody Usuario updatedUser) {
        Usuario usuario = userService.actualizarUsuario(id, updatedUser);
        return ResponseEntity.ok(usuario);
    }
}
