package com.belezaagil.service;

import com.belezaagil.dto.ComandaDto;
import com.belezaagil.entity.Comanda;
import com.belezaagil.mapper.EntityMapper;
import com.belezaagil.repository.ComandaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ComandaService {

    private final ComandaRepository comandaRepository;

    public ComandaService(ComandaRepository comandaRepository) {
        this.comandaRepository = comandaRepository;
    }

    public ComandaDto criar() {
        Comanda comanda = comandaRepository.save(new Comanda());
        return EntityMapper.toDto(comanda);
    }

    @Transactional(readOnly = true)
    public int getProximoNumero() {
        return comandaRepository.getProximoNumero();
    }
}
