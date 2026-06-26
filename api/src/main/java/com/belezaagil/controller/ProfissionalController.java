package com.belezaagil.controller;

import com.belezaagil.dto.*;
import com.belezaagil.service.ProfissionalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping
    public List<ProfissionalDto> listar(@RequestParam(required = false) String search) {
        return profissionalService.listar(search);
    }

    @GetMapping("/count")
    public CountResponse contar() {
        return new CountResponse(profissionalService.contar());
    }

    @GetMapping("/cpf-exists")
    public ExistsResponse cpfExists(
            @RequestParam String cpf,
            @RequestParam(required = false) Integer excludeId) {
        return new ExistsResponse(profissionalService.cpfExists(cpf, excludeId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfissionalDto criar(@RequestBody ProfissionalDto dto) {
        return profissionalService.salvar(new ProfissionalDto(
                null, dto.nome(), dto.dataNascimento(), dto.cpf(), dto.email(), dto.funcao()));
    }

    @PutMapping("/{id}")
    public ProfissionalDto atualizar(@PathVariable Integer id, @RequestBody ProfissionalDto dto) {
        return profissionalService.salvar(new ProfissionalDto(
                id, dto.nome(), dto.dataNascimento(), dto.cpf(), dto.email(), dto.funcao()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        profissionalService.excluir(id);
    }
}
