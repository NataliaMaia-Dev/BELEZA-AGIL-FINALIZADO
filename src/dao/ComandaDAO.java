package dao;

import dto.AgendamentoDTO;
import dto.ComandaDTO;
import dto.ProfissionalDTO;
import dto.ServicoDTO;
import repository.ComandaRepository;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComandaDAO implements ComandaRepository {

    @Override
    public void salvar(ComandaDTO comanda) throws SQLException {
        String sql = "INSERT INTO comandas () VALUES ()";

        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.executeUpdate();

            try ( ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comanda.setIdComanda(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public List<ComandaDTO> listarTodos() throws SQLException {
        List<ComandaDTO> comandas = new ArrayList<>();
        String sql = "SELECT * FROM comandas";

        try ( Connection conn = DatabaseConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ComandaDTO comanda = new ComandaDTO();
                comanda.setIdComanda(rs.getInt("id_comanda"));
                comandas.add(comanda);
            }
        }

        return comandas;
    }

    @Override
    public void excluir(int idComanda) throws SQLException {
        String sql = "DELETE FROM comandas WHERE id_comanda = ?";

        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idComanda);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<AgendamentoDTO> listarItensPorComanda(int idComanda) throws SQLException {
        List<AgendamentoDTO> lista = new ArrayList<>();

        String sql = """
        SELECT 
            a.id_agendamento,
            s.id_servico,
            s.nome AS nome_servico,
            s.valor,
            p.id_profissional,
            p.nome AS nome_profissional
        FROM agendamento a
        INNER JOIN servico s ON s.id_servico = a.id_servico
        INNER JOIN profissional p ON p.id_profissional = a.id_profissional
        WHERE a.id_comanda = ?
    """;

        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idComanda);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ServicoDTO servico = new ServicoDTO(
                        rs.getInt("id_servico"),
                        rs.getString("nome_servico"),
                        rs.getBigDecimal("valor")
                );

                ProfissionalDTO prof = new ProfissionalDTO();
                prof.setIdProfissional(rs.getInt("id_profissional"));
                prof.setNome(rs.getString("nome_profissional"));

                AgendamentoDTO ag = new AgendamentoDTO();
                ag.setServico(servico);
                ag.setProfissional(prof);

                lista.add(ag);
            }
        }

        return lista;
    }

    @Override
    public int getProximoNumeroComanda() throws SQLException {
        String sql = "SELECT MAX(id_comanda) AS ultimo_id FROM comandas";

        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int ultimoId = rs.getInt("ultimo_id");
                return ultimoId + 1; 
            }
        }
        return 1;
    }
}
