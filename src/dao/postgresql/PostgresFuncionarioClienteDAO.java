package dao.postgresql;

import dao.FuncionarioClienteDAO;
import dao.PersistenceException;
import model.EmpresaCliente;
import model.FuncionarioCliente;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresFuncionarioClienteDAO implements FuncionarioClienteDAO {

    private final ConnectionFactory connectionFactory;

    public PostgresFuncionarioClienteDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private FuncionarioCliente mapRow(ResultSet rs) throws SQLException {
        FuncionarioCliente f = new FuncionarioCliente();
        try {
            FuncionarioCliente.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(f, rs.getInt("id_funcionarios"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear ID de funcionário_cliente.", e);
        }
        EmpresaCliente emp = new EmpresaCliente();
        try {
            EmpresaCliente.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(emp, rs.getInt("id_empresa"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear empresa do funcionário_cliente.", e);
        }
        f.setEmpresa(emp);
        f.setNome(rs.getString("nome"));
        f.setCargo(rs.getString("cargo"));
        f.setMatricula(rs.getString("matricula"));
        return f;
    }

    @Override
    public Optional<FuncionarioCliente> findById(Integer id) {
        String sql = "SELECT * FROM funcionarios_cliente WHERE id_funcionarios = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar funcionario_cliente.", e);
        }
    }

    @Override
    public List<FuncionarioCliente> findAll() {
        String sql = "SELECT * FROM funcionarios_cliente";
        List<FuncionarioCliente> lista = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar funcionarios_cliente.", e);
        }
    }

    @Override
    public void insert(FuncionarioCliente f) {
        String sql = "INSERT INTO funcionarios_cliente " +
                "(id_empresa, nome, cargo, matricula) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, f.getEmpresa().getId());
            ps.setString(2, f.getNome());
            ps.setString(3, f.getCargo());
            ps.setString(4, f.getMatricula());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                FuncionarioCliente.class.getSuperclass()
                        .getDeclaredMethod("setId", Integer.class)
                        .invoke(f, keys.getInt(1));
            }
        } catch (Exception e) {
            throw new PersistenceException("Erro ao inserir funcionario_cliente.", e);
        }
    }

    @Override
    public void update(FuncionarioCliente f) {
        String sql = "UPDATE funcionarios_cliente SET " +
                "id_empresa=?, nome=?, cargo=?, matricula=? WHERE id_funcionarios=?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getEmpresa().getId());
            ps.setString(2, f.getNome());
            ps.setString(3, f.getCargo());
            ps.setString(4, f.getMatricula());
            ps.setInt(5, f.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar funcionario_cliente.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM funcionarios_cliente WHERE id_funcionarios = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao excluir funcionario_cliente.", e);
        }
    }
}
