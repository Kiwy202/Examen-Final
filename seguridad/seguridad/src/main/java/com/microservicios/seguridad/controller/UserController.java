package com.microservicios.seguridad.controller;

import com.microservicios.seguridad.model.Usuario;
import com.microservicios.seguridad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario updatedUser) {
        Usuario usuario = userService.actualizarUsuario(id, updatedUser);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/users/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> actualizarAdmin(@PathVariable Long id, @RequestBody Usuario updatedUser) {
        Usuario usuario = userService.actualizarUsuario(id, updatedUser);
        return ResponseEntity.ok(usuario);
    }
}