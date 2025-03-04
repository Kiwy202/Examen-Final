package com.microservicios.empresas.service;

import com.microservicios.empresas.model.Empresa;
import com.microservicios.empresas.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public List<Empresa> obtenerTodas() {
        return empresaRepository.findAll();
    }

    public Empresa obtenerPorRuc(String ruc) {
        return empresaRepository.findByRuc(ruc)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con RUC: " + ruc));
    }

    public Empresa crearEmpresa(Empresa empresa, String usuarioRegistro) {
        empresa.setUsuarioRegistro(usuarioRegistro);
        return empresaRepository.save(empresa);
    }

    public Empresa actualizarEmpresa(Long id, Empresa empresa) {
        Optional<Empresa> empresaExistente = empresaRepository.findById(id);
        if (empresaExistente.isEmpty()) {
            throw new RuntimeException("Empresa no encontrada");
        }
        empresa.setId(id);
        return empresaRepository.save(empresa);
    }

    public void eliminarEmpresa(Long id) {
        empresaRepository.deleteById(id);
    }
}