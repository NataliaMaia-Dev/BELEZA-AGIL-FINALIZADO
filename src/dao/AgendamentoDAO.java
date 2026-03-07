package dao;

import dto.AgendamentoDTO;
import dto.ClienteDTO;
import dto.ProfissionalDTO;
import dto.ServicoDTO;
import dto.ComandaDTO;
import repository.AgendamentoRepository;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO implements AgendamentoRepository {

    public void salvar(AgendamentoDTO agendamento) throws SQLException {
        String sqlAgendamento = """
        INSERT INTO agendamentos 
        (data_agendamento, cliente_id, servico_id, profissional_id, comanda_id, finalizado)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        String sqlHorario = "INSERT INTO agendamento_horarios (agendamento_id, horario) VALUES (?, ?)";

        try ( Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try ( PreparedStatement stmt = conn.prepareStatement(sqlAgendamento, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setTimestamp(1, Timestamp.valueOf(agendamento.getDataAgendamento()));
                stmt.setInt(2, agendamento.getCliente().getIdCliente());
                stmt.setInt(3, agendamento.getServico().getIdServico());
                stmt.setInt(4, agendamento.getProfissional().getIdProfissional());

                if (agendamento.getComanda() != null && agendamento.getComanda().getIdComanda() > 0) {
                    stmt.setInt(5, agendamento.getComanda().getIdComanda());
                } else {
                    stmt.setNull(5, Types.INTEGER);
                }

                stmt.setBoolean(6, agendamento.isFinalizado());
                stmt.executeUpdate();

                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        agendamento.setIdAgendamento(rs.getInt(1));
                    }
                }

                if (agendamento.getHorariosOcupados() != null && !agendamento.getHorariosOcupados().isEmpty()) {
                    try ( PreparedStatement stmtHorario = conn.prepareStatement(sqlHorario)) {
                        for (String horario : agendamento.getHorariosOcupados()) {
                            stmtHorario.setInt(1, agendamento.getIdAgendamento());
                            stmtHorario.setString(2, horario);
                            stmtHorario.addBatch();
                        }
                        stmtHorario.executeBatch();
                    }
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<AgendamentoDTO> listarTodos() throws SQLException {
        List<AgendamentoDTO> agendamentos = new ArrayList<>();

        String sql = "SELECT a.id_agendamento, a.data_agendamento, a.finalizado, "
                + "c.id_cliente, c.nome AS cliente_nome, "
                + "s.id_servico, s.nome AS servico_nome, s.tempo_execucao_minutes, s.valor, "
                + "p.id_profissional, p.nome AS profissional_nome, "
                + "co.id_comanda "
                + "FROM agendamentos a "
                + "JOIN clientes c ON a.cliente_id = c.id_cliente "
                + "JOIN servicos s ON a.servico_id = s.id_servico "
                + "JOIN profissionais p ON a.profissional_id = p.id_profissional "
                + "LEFT JOIN comandas co ON a.comanda_id = co.id_comanda";

        try ( Connection conn = DatabaseConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AgendamentoDTO agendamento = new AgendamentoDTO();

                agendamento.setIdAgendamento(rs.getInt("id_agendamento"));
                agendamento.setDataAgendamento(rs.getTimestamp("data_agendamento").toLocalDateTime());
                agendamento.setFinalizado(rs.getBoolean("finalizado"));

                ClienteDTO cliente = new ClienteDTO();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("cliente_nome"));
                agendamento.setCliente(cliente);

                ServicoDTO servico = new ServicoDTO();
                servico.setIdServico(rs.getInt("id_servico"));
                servico.setNome(rs.getString("servico_nome"));
                servico.setTempoExecucao(Duration.ofMinutes(rs.getInt("tempo_execucao_minutes")));
                servico.setValor(rs.getBigDecimal("valor"));
                agendamento.setServico(servico);

                ProfissionalDTO profissional = new ProfissionalDTO();
                profissional.setIdProfissional(rs.getInt("id_profissional"));
                profissional.setNome(rs.getString("profissional_nome"));
                agendamento.setProfissional(profissional);

                int comandaId = rs.getInt("id_comanda");
                if (!rs.wasNull()) {
                    ComandaDTO comanda = new ComandaDTO();
                    comanda.setIdComanda(comandaId);
                    agendamento.setComanda(comanda);
                }

                String sqlHorarios = "SELECT horario FROM agendamento_horarios WHERE agendamento_id = ?";
                try ( PreparedStatement stmtHorarios = conn.prepareStatement(sqlHorarios)) {
                    stmtHorarios.setInt(1, agendamento.getIdAgendamento());
                    try ( ResultSet rsHorarios = stmtHorarios.executeQuery()) {
                        List<String> horarios = new ArrayList<>();
                        while (rsHorarios.next()) {
                            horarios.add(rsHorarios.getString("horario"));
                        }
                        agendamento.setHorariosOcupados(horarios);
                    }
                }

                agendamentos.add(agendamento);
            }
        }

        return agendamentos;
    }

    public void atualizarFinalizado(int idAgendamento, boolean finalizado) throws SQLException {
        String sql = "UPDATE agendamentos SET finalizado = ? WHERE id_agendamento = ?";
        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, finalizado);
            stmt.setInt(2, idAgendamento);
            stmt.executeUpdate();
        }
    }

    public void excluir(int idAgendamento) throws SQLException {
        String sql = "DELETE FROM agendamentos WHERE id_agendamento = ?";
        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAgendamento);
            stmt.executeUpdate();
        }
    }

    public void finalizarComanda(int comandaId) throws SQLException {
        String sql = "UPDATE agendamentos SET finalizado = 1 WHERE comanda_id = ?";
        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, comandaId);
            stmt.executeUpdate();
        }
    }

    public void excluirPorComanda(int comandaId) throws SQLException {
        String sql = "DELETE FROM agendamentos WHERE comanda_id = ?";
        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, comandaId);
            stmt.executeUpdate();
        }
    }


    public List<AgendamentoDTO> listarPorData(LocalDate data) throws SQLException {
    List<AgendamentoDTO> agendamentos = new ArrayList<>();

    String sql = """
        SELECT a.id_agendamento, a.data_agendamento, a.finalizado,
               c.id_cliente, c.nome AS cliente_nome,
               s.id_servico, s.nome AS servico_nome, s.tempo_execucao_minutes, s.valor,
               p.id_profissional, p.nome AS profissional_nome,
               co.id_comanda
        FROM agendamentos a
        JOIN clientes c ON a.cliente_id = c.id_cliente
        JOIN servicos s ON a.servico_id = s.id_servico
        JOIN profissionais p ON a.profissional_id = p.id_profissional
        LEFT JOIN comandas co ON a.comanda_id = co.id_comanda
        WHERE DATE(a.data_agendamento) = ?
        ORDER BY a.data_agendamento
    """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setDate(1, java.sql.Date.valueOf(data));

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AgendamentoDTO agendamento = new AgendamentoDTO();

                agendamento.setIdAgendamento(rs.getInt("id_agendamento"));
                agendamento.setDataAgendamento(rs.getTimestamp("data_agendamento").toLocalDateTime());
                agendamento.setFinalizado(rs.getBoolean("finalizado"));

                ClienteDTO cliente = new ClienteDTO();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("cliente_nome"));
                agendamento.setCliente(cliente);

                ServicoDTO servico = new ServicoDTO();
                servico.setIdServico(rs.getInt("id_servico"));
                servico.setNome(rs.getString("servico_nome"));
                servico.setTempoExecucao(Duration.ofMinutes(rs.getInt("tempo_execucao_minutes")));
                servico.setValor(rs.getBigDecimal("valor"));
                agendamento.setServico(servico);

                ProfissionalDTO profissional = new ProfissionalDTO();
                profissional.setIdProfissional(rs.getInt("id_profissional"));
                profissional.setNome(rs.getString("profissional_nome"));
                agendamento.setProfissional(profissional);

                int comandaId = rs.getInt("id_comanda");
                if (!rs.wasNull()) {
                    ComandaDTO comanda = new ComandaDTO();
                    comanda.setIdComanda(comandaId);
                    agendamento.setComanda(comanda);
                }

                String sqlHorarios = "SELECT horario FROM agendamento_horarios WHERE agendamento_id = ?";
                try (PreparedStatement stmtHorarios = conn.prepareStatement(sqlHorarios)) {
                    stmtHorarios.setInt(1, agendamento.getIdAgendamento());
                    try (ResultSet rsHorarios = stmtHorarios.executeQuery()) {
                        List<String> horarios = new ArrayList<>();
                        while (rsHorarios.next()) {
                            horarios.add(rsHorarios.getString("horario"));
                        }
                        agendamento.setHorariosOcupados(horarios);
                    }
                }

                agendamentos.add(agendamento);
            }
        }
    }

    return agendamentos;
}
}
