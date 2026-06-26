package com.belezaagil.controller;

import com.belezaagil.dto.AgendamentoDto;
import com.belezaagil.dto.AgendamentoLoteRequest;
import com.belezaagil.service.AgendamentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public List<AgendamentoDto> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return agendamentoService.listar(data);
    }

    @PostMapping("/lote")
    @ResponseStatus(HttpStatus.CREATED)
    public List<AgendamentoDto> salvarLote(@RequestBody AgendamentoLoteRequest request) {
        return agendamentoService.salvarLote(request);
    }

    @PatchMapping("/comanda/{comandaId}/finalizar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finalizarComanda(@PathVariable Integer comandaId) {
        agendamentoService.finalizarComanda(comandaId);
    }

    @DeleteMapping("/comanda/{comandaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirComanda(@PathVariable Integer comandaId) {
        agendamentoService.excluirPorComanda(comandaId);
    }
}
