package dao;

import dto.ServicoDTO;
import repository.ServicoRepository;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO implements ServicoRepository {

    public void salvar(ServicoDTO servico) throws SQLException {
        String sql = "INSERT INTO servicos (nome, tempo_execucao_minutes, valor) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, servico.getNome());
            stmt.setInt(2, (int) servico.getTempoExecucao().toMinutes()); 
            stmt.setBigDecimal(3, servico.getValor());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    servico.setIdServico(generatedKeys.getInt(1));
                }
            }
        }
    }


    public List<ServicoDTO> listarTodos() throws SQLException {
        List<ServicoDTO> servicos = new ArrayList<>();
        String sql = "SELECT * FROM servicos";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ServicoDTO s = new ServicoDTO();
                s.setIdServico(rs.getInt("id_servico"));
                s.setNome(rs.getString("nome"));
                int minutos = rs.getInt("tempo_execucao_minutes");
                s.setTempoExecucao(Duration.ofMinutes(minutos)); 
                s.setValor(rs.getBigDecimal("valor"));

                servicos.add(s);
            }
        }

        return servicos;
    }

    public void atualizar(ServicoDTO servico) throws SQLException {
        String sql = "UPDATE servicos SET nome = ?, tempo_execucao_minutes = ?, valor = ? WHERE id_servico = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, servico.getNome());
            stmt.setInt(2, (int) servico.getTempoExecucao().toMinutes());
            stmt.setBigDecimal(3, servico.getValor());
            stmt.setInt(4, servico.getIdServico());

            stmt.executeUpdate();
        }
    }


    public void excluir(int idServico) throws SQLException {
        String sql = "DELETE FROM servicos WHERE id_servico = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idServico);
            stmt.executeUpdate();
        }
    }
}
