package com.belezaagil.service;

import com.belezaagil.dto.ServicoDto;
import com.belezaagil.entity.Servico;
import com.belezaagil.exception.BusinessException;
import com.belezaagil.mapper.EntityMapper;
import com.belezaagil.repository.ServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @Transactional(readOnly = true)
    public List<ServicoDto> listar(String search) {
        List<Servico> servicos = (search == null || search.isBlank())
                ? servicoRepository.findAll()
                : servicoRepository.search(search.trim());
        return servicos.stream().map(EntityMapper::toDto).toList();
    }

    public ServicoDto salvar(ServicoDto dto) {
        if (dto.tempoExecucao() == null || dto.tempoExecucao() <= 0) {
            throw new BusinessException("Tempo de execução deve ser maior que zero.");
        }
        Servico entity = dto.id() != null
                ? servicoRepository.findById(dto.id())
                    .orElseThrow(() -> new BusinessException("Serviço não encontrado."))
                : new Servico();

        entity.setNome(dto.nome());
        entity.setTempoExecucaoMinutes(dto.tempoExecucao());
        entity.setValor(dto.valor());

        return EntityMapper.toDto(servicoRepository.save(entity));
    }

    public void excluir(Integer id) {
        if (!servicoRepository.existsById(id)) {
            throw new BusinessException("Serviço não encontrado.");
        }
        servicoRepository.deleteById(id);
    }
}
