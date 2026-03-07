package view;

import dto.ClienteDTO;
import dto.ProfissionalDTO;
import dto.ServicoDTO;
import repository.ClienteRepository;
import repository.ProfissionalRepository;
import repository.ServicoRepository;
import service.ClienteService;
import service.ProfissionalService;
import service.ServicoService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Esmalteria {

    public static void main(String[] args) {

        // ── Teste 1: Cadastrar Cliente ─────────────────────────────────────────
        try {
            List<ClienteDTO> banco = new ArrayList<>();

            ClienteService service = new ClienteService(new ClienteRepository() {
                public void salvar(ClienteDTO c)      throws SQLException { banco.add(c); }
                public List<ClienteDTO> listarTodos() throws SQLException { return banco; }
                public void atualizar(ClienteDTO c)   throws SQLException {}
                public void excluir(int id)           throws SQLException {}
            });

            ClienteDTO cliente = new ClienteDTO(1, "Ana Paula", new Date(), "123.456.789-00", "(51) 91234-5678");
            service.salvar(cliente);

            if (banco.size() == 1 && "Ana Paula".equals(banco.get(0).getNome())) {
                System.out.println("[PASSOU] Cadastrar Cliente");
            } else {
                System.out.println("[FALHOU] Cadastrar Cliente — cliente não foi salvo corretamente");
            }
        } catch (Exception e) {
            System.out.println("[FALHOU] Cadastrar Cliente — " + e.getMessage());
        }

        // ── Teste 2: Cadastrar Profissional ────────────────────────────────────
        try {
            List<ProfissionalDTO> banco = new ArrayList<>();

            ProfissionalService service = new ProfissionalService(new ProfissionalRepository() {
                public void salvar(ProfissionalDTO p)      throws SQLException { banco.add(p); }
                public List<ProfissionalDTO> listarTodos() throws SQLException { return banco; }
                public void atualizar(ProfissionalDTO p)   throws SQLException {}
                public void excluir(int id)                throws SQLException {}
                public int contarProfissionais()           throws SQLException { return banco.size(); }
            });

            ProfissionalDTO profissional = new ProfissionalDTO(
                1, "Camila Souza", LocalDate.of(1995, 3, 20),
                "555.666.777-88", "camila@salao.com", "Manicure"
            );
            service.salvar(profissional);

            if (banco.size() == 1 && "Camila Souza".equals(banco.get(0).getNome())) {
                System.out.println("[PASSOU] Cadastrar Profissional");
            } else {
                System.out.println("[FALHOU] Cadastrar Profissional — profissional não foi salvo corretamente");
            }
        } catch (Exception e) {
            System.out.println("[FALHOU] Cadastrar Profissional — " + e.getMessage());
        }

        // ── Teste 3: Cadastrar Serviço ─────────────────────────────────────────
        try {
            List<ServicoDTO> banco = new ArrayList<>();

            ServicoService service = new ServicoService(new ServicoRepository() {
                public void salvar(ServicoDTO s)      throws SQLException { banco.add(s); }
                public List<ServicoDTO> listarTodos() throws SQLException { return banco; }
                public void atualizar(ServicoDTO s)   throws SQLException {}
                public void excluir(int id)           throws SQLException {}
            });

            ServicoDTO servico = new ServicoDTO(1, "Esmaltação Simples", Duration.ofMinutes(30), new BigDecimal("45.00"));
            service.salvar(servico);

            if (banco.size() == 1 && "Esmaltação Simples".equals(banco.get(0).getNome())) {
                System.out.println("[PASSOU] Cadastrar Serviço");
            } else {
                System.out.println("[FALHOU] Cadastrar Serviço — serviço não foi salvo corretamente");
            }
        } catch (Exception e) {
            System.out.println("[FALHOU] Cadastrar Serviço — " + e.getMessage());
        }
    }
}
