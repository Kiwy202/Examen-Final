package com.microservicios.empresas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ruc; // Nuevo campo RUC

    private String nombre;
    private String direccion;
    private String telefono;
    private String usuarioRegistro; // Guarda quién creó la empresa
}