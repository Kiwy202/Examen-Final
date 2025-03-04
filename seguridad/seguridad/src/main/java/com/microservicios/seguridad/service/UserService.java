package com.microservicios.seguridad.service;

import com.microservicios.seguridad.model.Usuario;

public interface UserService {
    Usuario actualizarUsuario(Long id, Usuario usuario);
}