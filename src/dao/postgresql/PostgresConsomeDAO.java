package dao.postgresql;

import dao.ConsomeDAO;
import dao.PersistenceException;
import model.Consumo;
import model.FuncionarioCliente;
import model.Refeicao;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresConsomeDAO implements ConsomeDAO {

    private final ConnectionFactory connectionFactory;

    public PostgresConsomeDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private Consumo mapRow(ResultSet rs) throws SQLException {
        Consumo c = new Consumo();
        try {
            Consumo.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(c, rs.getInt("id_consumo"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear ID de consumo.", e);
        }
        FuncionarioCliente f = new FuncionarioCliente();
        Refeicao r = new Refeicao();
        try {
            FuncionarioCliente.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(f, rs.getInt("id_funcionarios"));
            Refeicao.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(r, rs.getInt("id_refeicao"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear chaves de consumo.", e);
        }
        c.setFuncionario(f);
        c.setRefeicao(r);
        return c;
    }

    @Override
    public Optional<Consumo> findById(Integer id) {
        String sql = "SELECT * FROM consome WHERE id_consumo = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar consumo.", e);
        }
    }

    @Override
    public List<Consumo> findAll() {
        String sql = "SELECT * FROM consome";
        List<Consumo> lista = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar consumos.", e);
        }
    }

    @Override
    public void insert(Consumo c) {
        String sql = "INSERT INTO consome (id_funcionarios, id_refeicao) VALUES (?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getFuncionario().getId());
            ps.setInt(2, c.getRefeicao().getId());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                Consumo.class.getSuperclass()
                        .getDeclaredMethod("setId", Integer.class)
                        .invoke(c, keys.getInt(1));
            }
        } catch (Exception e) {
            throw new PersistenceException("Erro ao inserir consumo.", e);
        }
    }

    @Override
    public void update(Consumo c) {
        String sql = "UPDATE consome SET id_funcionarios=?, id_refeicao=? WHERE id_consumo=?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getFuncionario().getId());
            ps.setInt(2, c.getRefeicao().getId());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar consumo.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM consome WHERE id_consumo = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao excluir consumo.", e);
        }
    }
}