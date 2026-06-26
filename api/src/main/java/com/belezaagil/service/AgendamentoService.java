package com.belezaagil.service;

import com.belezaagil.dto.AgendamentoDto;
import com.belezaagil.dto.AgendamentoLoteRequest;
import com.belezaagil.dto.AgendamentoRequest;
import com.belezaagil.entity.*;
import com.belezaagil.exception.BusinessException;
import com.belezaagil.mapper.EntityMapper;
import com.belezaagil.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AgendamentoService {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ComandaRepository comandaRepository;
    private final ComandaService comandaService;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            ClienteRepository clienteRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            ComandaRepository comandaRepository,
            ComandaService comandaService) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
        this.comandaRepository = comandaRepository;
        this.comandaService = comandaService;
    }

    @Transactional(readOnly = true)
    public List<AgendamentoDto> listar(LocalDate data) {
        List<Agendamento> agendamentos = data != null
                ? agendamentoRepository.findByData(data)
                : agendamentoRepository.findAllWithDetails();
        return agendamentos.stream().map(EntityMapper::toDto).toList();
    }

    public List<AgendamentoDto> salvarLote(AgendamentoLoteRequest request) {
        if (request.agendamentos() == null || request.agendamentos().isEmpty()) {
            throw new BusinessException("Nenhum agendamento informado.");
        }

        Comanda comanda = comandaRepository.findById(comandaService.criar().id())
                .orElseThrow(() -> new BusinessException("Erro ao criar comanda."));

        List<AgendamentoRequest> pendentes = new ArrayList<>();
        for (AgendamentoRequest req : request.agendamentos()) {
            validarConflito(req, pendentes);
            pendentes.add(req);
        }

        List<AgendamentoDto> salvos = new ArrayList<>();
        for (AgendamentoRequest req : request.agendamentos()) {
            salvos.add(salvarAgendamento(req, comanda));
        }
        return salvos;
    }

    public void finalizarComanda(Integer comandaId) {
        agendamentoRepository.finalizarComanda(comandaId);
    }

    public void excluirPorComanda(Integer comandaId) {
        agendamentoRepository.deleteByComandaId(comandaId);
    }

    private AgendamentoDto salvarAgendamento(AgendamentoRequest req, Comanda comanda) {
        Cliente cliente = clienteRepository.findById(req.clienteId())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));
        Servico servico = servicoRepository.findById(req.servicoId())
                .orElseThrow(() -> new BusinessException("Serviço não encontrado."));
        Profissional profissional = profissionalRepository.findById(req.profissionalId())
                .orElseThrow(() -> new BusinessException("Profissional não encontrado."));

        LocalDate data = LocalDate.parse(req.data());
        if (data.isBefore(LocalDate.now())) {
            throw new BusinessException("Data de agendamento não pode ser no passado.");
        }

        LocalTime hora = LocalTime.parse(req.horario(), TIME_FORMAT);
        Agendamento agendamento = new Agendamento();
        agendamento.setDataAgendamento(LocalDateTime.of(data, hora));
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setProfissional(profissional);
        agendamento.setComanda(comanda);
        agendamento.setFinalizado(false);

        if (req.horariosOcupados() != null) {
            for (String horario : req.horariosOcupados()) {
                agendamento.addHorario(horario);
            }
        }

        return EntityMapper.toDto(agendamentoRepository.save(agendamento));
    }

    private void validarConflito(AgendamentoRequest req, List<AgendamentoRequest> pendentes) {
        LocalDate data = LocalDate.parse(req.data());
        List<Agendamento> existentes = agendamentoRepository.findByDataAndProfissional(data, req.profissionalId());

        Set<String> horariosReq = new HashSet<>(req.horariosOcupados());

        for (Agendamento existente : existentes) {
            for (AgendamentoHorario h : existente.getHorarios()) {
                if (horariosReq.contains(h.getHorario())) {
                    throw new BusinessException("Conflito de horário detectado para o profissional selecionado.");
                }
            }
        }

        for (AgendamentoRequest pendente : pendentes) {
            if (!pendente.profissionalId().equals(req.profissionalId())) {
                continue;
            }
            for (String horario : pendente.horariosOcupados()) {
                if (horariosReq.contains(horario)) {
                    throw new BusinessException("Conflito de horário detectado entre serviços da mesma comanda.");
                }
            }
        }
    }
}
