package dao.postgresql;

import dao.FuncionarioDAO;
import dao.PersistenceException;
import model.Funcionario;
import util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresFuncionarioDAO implements FuncionarioDAO {

    private final ConnectionFactory connectionFactory;

    public PostgresFuncionarioDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private Funcionario mapRow(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setCpf(rs.getString("cpf"));
        f.setCargo(rs.getString("cargo"));
        f.setEndereco(rs.getString("endereco"));
        f.setTelefone((Integer) rs.getObject("telefone"));
        f.setSalario(rs.getDouble("salario"));
        f.setDataAdmissao(rs.getObject("dt_admissao", LocalDate.class));
        f.setDataUltimoPagamento(rs.getObject("dt_ult_pgmt", LocalDate.class));
        return f;
    }

    // Implementações da interface genérica usando ID numérico podem lançar exceção,
    // já que aqui a PK é cpf. Opcionalmente, você pode mudar a interface.
    @Override
    public Optional<Funcionario> findById(Integer id) {
        throw new UnsupportedOperationException("Use findByCpf para Funcionario.");
    }

    public Optional<Funcionario> findByCpf(String cpf) {
        String sql = "SELECT * FROM funcionarios WHERE cpf = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar funcionario.", e);
        }
    }

    @Override
    public List<Funcionario> findAll() {
        String sql = "SELECT * FROM funcionarios";
        List<Funcionario> lista = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapRow(rs));
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar funcionarios.", e);
        }
    }

    @Override
    public void insert(Funcionario f) {
        String sql = "INSERT INTO funcionarios " +
                "(cpf, cargo, endereco, telefone, salario, dt_admissao, dt_ult_pgmt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, f.getCpf());
            ps.setString(2, f.getCargo());
            ps.setString(3, f.getEndereco());
            ps.setObject(4, f.getTelefone(), Types.INTEGER);
            ps.setDouble(5, f.getSalario());
            ps.setObject(6, f.getDataAdmissao());
            ps.setObject(7, f.getDataUltimoPagamento());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao inserir funcionario.", e);
        }
    }

    @Override
    public void update(Funcionario f) {
        String sql = "UPDATE funcionarios SET " +
                "cargo=?, endereco=?, telefone=?, salario=?, dt_admissao=?, dt_ult_pgmt=? " +
                "WHERE cpf=?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, f.getCargo());
            ps.setString(2, f.getEndereco());
            ps.setObject(3, f.getTelefone(), Types.INTEGER);
            ps.setDouble(4, f.getSalario());
            ps.setObject(5, f.getDataAdmissao());
            ps.setObject(6, f.getDataUltimoPagamento());
            ps.setString(7, f.getCpf());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar funcionario.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Use deleteByCpf para Funcionario.");
    }

    public void deleteByCpf(String cpf) {
        String sql = "DELETE FROM funcionarios WHERE cpf = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao excluir funcionario.", e);
        }
    }
}