package com.belezaagil.service;

import com.belezaagil.dto.ProfissionalDto;
import com.belezaagil.entity.Profissional;
import com.belezaagil.exception.BusinessException;
import com.belezaagil.mapper.EntityMapper;
import com.belezaagil.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final int limiteProfissionais;

    public ProfissionalService(
            ProfissionalRepository profissionalRepository,
            @Value("${beleza-agil.limite-profissionais}") int limiteProfissionais) {
        this.profissionalRepository = profissionalRepository;
        this.limiteProfissionais = limiteProfissionais;
    }

    @Transactional(readOnly = true)
    public List<ProfissionalDto> listar(String search) {
        List<Profissional> profissionais = (search == null || search.isBlank())
                ? profissionalRepository.findAll()
                : profissionalRepository.search(search.trim());
        return profissionais.stream().map(EntityMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public long contar() {
        return profissionalRepository.count();
    }

    public ProfissionalDto salvar(ProfissionalDto dto) {
        validarCpfUnico(dto.cpf(), dto.id());

        Profissional entity = dto.id() != null
                ? profissionalRepository.findById(dto.id())
                    .orElseThrow(() -> new BusinessException("Profissional não encontrado."))
                : new Profissional();

        if (dto.id() == null && profissionalRepository.count() >= limiteProfissionais) {
            throw new BusinessException("Limite máximo de " + limiteProfissionais + " profissionais atingido!");
        }

        entity.setNome(dto.nome());
        entity.setDataNascimento(dto.dataNascimento());
        entity.setCpf(dto.cpf());
        entity.setEmail(dto.email());
        entity.setFuncao(dto.funcao());

        return EntityMapper.toDto(profissionalRepository.save(entity));
    }

    public void excluir(Integer id) {
        if (!profissionalRepository.existsById(id)) {
            throw new BusinessException("Profissional não encontrado.");
        }
        profissionalRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean cpfExists(String cpf, Integer excludeId) {
        if (cpf == null || cpf.isBlank()) {
            return false;
        }
        if (excludeId != null) {
            return profissionalRepository.existsByCpfAndIdNot(cpf, excludeId);
        }
        return profissionalRepository.existsByCpf(cpf);
    }

    private void validarCpfUnico(String cpf, Integer excludeId) {
        if (cpfExists(cpf, excludeId)) {
            throw new BusinessException("CPF já cadastrado.");
        }
    }
}
