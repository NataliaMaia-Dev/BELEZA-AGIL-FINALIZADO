package dao;

import dto.ClienteDTO;
import repository.ClienteRepository;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements ClienteRepository {

    public void salvar(ClienteDTO cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, data_nascimento, cpf, telefone) VALUES (?, ?, ?, ?)";

        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            if (cliente.getDataNascimento() != null) {
                java.sql.Date sqlDate = new java.sql.Date(cliente.getDataNascimento().getTime());
                stmt.setDate(2, sqlDate);
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getTelefone());

            stmt.executeUpdate();

            try ( ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<ClienteDTO> listarTodos() throws SQLException {
        List<ClienteDTO> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try ( Connection conn = DatabaseConnection.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ClienteDTO cliente = new ClienteDTO();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setDataNascimento(rs.getDate("data_nascimento"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setTelefone(rs.getString("telefone"));

                clientes.add(cliente);
            }
        }

        return clientes;
    }

    public void atualizar(ClienteDTO cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, data_nascimento = ?, cpf = ?, telefone = ? WHERE id_cliente = ?";

        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            if (cliente.getDataNascimento() != null) {
                stmt.setDate(2, (Date) cliente.getDataNascimento());
            } else {
                stmt.setNull(2, Types.DATE);
            }
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getTelefone());
            stmt.setInt(5, cliente.getIdCliente());

            stmt.executeUpdate();
        }
    }

    public void excluir(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try ( Connection conn = DatabaseConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            stmt.executeUpdate();
        }
    }
}
