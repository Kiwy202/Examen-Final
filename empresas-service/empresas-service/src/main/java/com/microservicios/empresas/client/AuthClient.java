package com.microservicios.empresas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "seguridad-service", url = "http://localhost:8080/auth")
public interface AuthClient {

    @GetMapping("/user-info")
    String obtenerUsuario(@RequestHeader("Authorization") String token);
}