package com.belezaagil.controller;

import com.belezaagil.dto.ComandaDto;
import com.belezaagil.service.ComandaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comandas")
public class ComandaController {

    private final ComandaService comandaService;

    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComandaDto criar() {
        return comandaService.criar();
    }

    @GetMapping("/proximo-numero")
    public ComandaDto proximoNumero() {
        return new ComandaDto(comandaService.getProximoNumero());
    }
}
