package com.belezaagil.controller;

import com.belezaagil.dto.*;
import com.belezaagil.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteDto> listar(@RequestParam(required = false) String search) {
        return clienteService.listar(search);
    }

    @GetMapping("/{id}")
    public ClienteDto buscar(@PathVariable Integer id) {
        return clienteService.buscarPorId(id);
    }

    @GetMapping("/cpf-exists")
    public ExistsResponse cpfExists(
            @RequestParam String cpf,
            @RequestParam(required = false) Integer excludeId) {
        return new ExistsResponse(clienteService.cpfExists(cpf, excludeId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDto criar(@RequestBody ClienteDto dto) {
        return clienteService.salvar(new ClienteDto(null, dto.nome(), dto.dataNascimento(), dto.cpf(), dto.telefone()));
    }

    @PutMapping("/{id}")
    public ClienteDto atualizar(@PathVariable Integer id, @RequestBody ClienteDto dto) {
        return clienteService.salvar(new ClienteDto(id, dto.nome(), dto.dataNascimento(), dto.cpf(), dto.telefone()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        clienteService.excluir(id);
    }
}
