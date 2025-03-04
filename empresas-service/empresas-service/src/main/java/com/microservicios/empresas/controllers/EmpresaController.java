package com.microservicios.empresas.controllers;

import com.microservicios.empresas.model.Empresa;
import com.microservicios.empresas.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    // Obtener todas las empresas
    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        List<Empresa> empresas = empresaService.obtenerTodas();
        return ResponseEntity.ok(empresas);
    }

    // ✅ Nuevo: Obtener empresa por RUC
    @GetMapping("/{ruc}")
    public ResponseEntity<Empresa> obtenerEmpresaPorRuc(@PathVariable String ruc) {
        Empresa empresa = empresaService.obtenerPorRuc(ruc);
        return ResponseEntity.ok(empresa);
    }

    // Crear una nueva empresa
    @PostMapping
    public ResponseEntity<Empresa> crearEmpresa(@RequestBody Empresa empresa, @AuthenticationPrincipal UserDetails userDetails) {
        Empresa nuevaEmpresa = empresaService.crearEmpresa(empresa, userDetails.getUsername());
        return ResponseEntity.ok(nuevaEmpresa);
    }

    // Actualizar una empresa
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
        Empresa empresaActualizada = empresaService.actualizarEmpresa(id, empresa);
        return ResponseEntity.ok(empresaActualizada);
    }

    // Eliminar una empresa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable Long id) {
        empresaService.eliminarEmpresa(id);
        return ResponseEntity.noContent().build();
    }
}