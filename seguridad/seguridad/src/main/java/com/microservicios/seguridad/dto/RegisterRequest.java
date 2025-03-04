package com.microservicios.seguridad.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String nombre;
    private String username;
    private String password;
    private String rol;
}