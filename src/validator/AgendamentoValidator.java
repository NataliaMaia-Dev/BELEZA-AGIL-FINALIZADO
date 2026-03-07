package validator;

import dto.ClienteDTO;
import dto.ProfissionalDTO;
import dto.ServicoDTO;

import java.util.Date;

public class AgendamentoValidator {

    public static void validarDadosObrigatorios(ClienteDTO cliente,
                                                Date data,
                                                ServicoDTO servico,
                                                ProfissionalDTO profissional) {
        if (cliente == null) {
            throw new IllegalArgumentException("Selecione um cliente.");
        }
        if (data == null) {
            throw new IllegalArgumentException("Selecione uma data válida.");
        }
        if (servico == null) {
            throw new IllegalArgumentException("Selecione um serviço.");
        }
        if (profissional == null) {
            throw new IllegalArgumentException("Selecione um profissional.");
        }
    }
}

