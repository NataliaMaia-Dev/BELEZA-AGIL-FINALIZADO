package dao;

import dto.ProfissionalDTO;
import repository.ProfissionalRepository;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfissionalDAO implements ProfissionalRepository {

    public void salvar(ProfissionalDTO profissional) throws SQLException {
        String sql = "INSERT INTO profissionais (nome, data_nascimento, cpf, email, funcao) "
                + "VALUES (?, ?, ?, ?, ?)";

        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, profissional.getNome());
            if (profissional.getDataNascimento() != null) {
                stmt.setDate(2, Date.valueOf(profissional.getDataNascimento()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            stmt.setString(3, profissional.getCpf());
            stmt.setString(4, profissional.getEmail());
            stmt.setString(5, profissional.getFuncao());

            stmt.executeUpdate();

            try ( ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    profissional.setIdProfissional(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<ProfissionalDTO> listarTodos() throws SQLException {
        List<ProfissionalDTO> profissionais = new ArrayList<>();
        String sql = "SELECT id_profissional, nome, data_nascimento, cpf, email, funcao FROM profissionais";

        try ( Connection conn = DatabaseConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ProfissionalDTO p = new ProfissionalDTO();
                p.setIdProfissional(rs.getInt("id_profissional"));

                Date data = rs.getDate("data_nascimento");
                if (data != null) {
                    p.setDataNascimento(data.toLocalDate());
                }

                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setEmail(rs.getString("email"));
                p.setFuncao(rs.getString("funcao"));

                profissionais.add(p);
            }
        }

        return profissionais;
    }

    public void atualizar(ProfissionalDTO profissional) throws SQLException {
        String sql = "UPDATE profissionais SET nome = ?, data_nascimento = ?, cpf = ?, email = ?, funcao = ? "
                + "WHERE id_profissional = ?";

        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, profissional.getNome());
            if (profissional.getDataNascimento() != null) {
                stmt.setDate(2, Date.valueOf(profissional.getDataNascimento()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            stmt.setString(3, profissional.getCpf());
            stmt.setString(4, profissional.getEmail());
            stmt.setString(5, profissional.getFuncao());
            stmt.setInt(6, profissional.getIdProfissional());

            stmt.executeUpdate();
        }
    }

    public void excluir(int idProfissional) throws SQLException {
        String sql = "DELETE FROM profissionais WHERE id_profissional = ?";
        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProfissional);
            stmt.executeUpdate();
        }
    }

    public int contarProfissionais() throws SQLException {
        String sql = "SELECT COUNT(*) FROM profissionais";
        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
