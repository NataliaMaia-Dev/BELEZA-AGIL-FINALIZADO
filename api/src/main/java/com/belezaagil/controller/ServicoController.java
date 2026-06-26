package com.belezaagil.controller;

import com.belezaagil.dto.ServicoDto;
import com.belezaagil.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    public List<ServicoDto> listar(@RequestParam(required = false) String search) {
        return servicoService.listar(search);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoDto criar(@RequestBody ServicoDto dto) {
        return servicoService.salvar(new ServicoDto(null, dto.nome(), dto.tempoExecucao(), dto.valor()));
    }

    @PutMapping("/{id}")
    public ServicoDto atualizar(@PathVariable Integer id, @RequestBody ServicoDto dto) {
        return servicoService.salvar(new ServicoDto(id, dto.nome(), dto.tempoExecucao(), dto.valor()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        servicoService.excluir(id);
    }
}
