package com.microservicios.seguridad.controller;

import com.microservicios.seguridad.model.Usuario;
import com.microservicios.seguridad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

  
    @PutMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = userService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }
}