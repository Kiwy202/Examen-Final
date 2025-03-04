package com.microservicios.seguridad.service;

import com.microservicios.seguridad.model.Usuario;
import java.util.List;

public interface UserService {
    Usuario actualizarUsuario(Long id, Usuario usuario);
    List<Usuario> obtenerUsuariosPorRol(String rol);
}
