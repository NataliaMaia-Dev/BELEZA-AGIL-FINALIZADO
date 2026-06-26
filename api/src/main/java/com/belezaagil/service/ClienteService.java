package com.belezaagil.service;

import com.belezaagil.dto.ClienteDto;
import com.belezaagil.entity.Cliente;
import com.belezaagil.exception.BusinessException;
import com.belezaagil.mapper.EntityMapper;
import com.belezaagil.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteDto> listar(String search) {
        List<Cliente> clientes = (search == null || search.isBlank())
                ? clienteRepository.findAll()
                : clienteRepository.search(search.trim());
        return clientes.stream().map(EntityMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ClienteDto buscarPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));
        return EntityMapper.toDto(cliente);
    }

    public ClienteDto salvar(ClienteDto dto) {
        validarCpfUnico(dto.cpf(), dto.id());

        Cliente entity = dto.id() != null
                ? clienteRepository.findById(dto.id())
                    .orElseThrow(() -> new BusinessException("Cliente não encontrado."))
                : new Cliente();

        entity.setNome(dto.nome());
        entity.setDataNascimento(dto.dataNascimento());
        entity.setCpf(dto.cpf());
        entity.setTelefone(dto.telefone());

        return EntityMapper.toDto(clienteRepository.save(entity));
    }

    public void excluir(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new BusinessException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean cpfExists(String cpf, Integer excludeId) {
        if (cpf == null || cpf.isBlank()) {
            return false;
        }
        if (excludeId != null) {
            return clienteRepository.existsByCpfAndIdNot(cpf, excludeId);
        }
        return clienteRepository.existsByCpf(cpf);
    }

    private void validarCpfUnico(String cpf, Integer excludeId) {
        if (cpfExists(cpf, excludeId)) {
            throw new BusinessException("CPF já cadastrado.");
        }
    }
}
