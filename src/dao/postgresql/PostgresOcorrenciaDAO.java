package dao.postgresql;

import dao.OcorrenciaDAO;
import dao.PersistenceException;
import model.FuncionarioCliente;
import model.Ocorrencia;
import util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresOcorrenciaDAO implements OcorrenciaDAO {

    private final ConnectionFactory connectionFactory;

    public PostgresOcorrenciaDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private Ocorrencia mapRow(ResultSet rs) throws SQLException {
        Ocorrencia o = new Ocorrencia();
        try {
            Ocorrencia.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(o, rs.getInt("id_ocorrencia"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear ID de ocorrencia.", e);
        }

        FuncionarioCliente f = new FuncionarioCliente();
        try {
            FuncionarioCliente.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(f, rs.getInt("id_funcionarios"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear funcionário da ocorrencia.", e);
        }

        o.setFuncionario(f);
        o.setData(rs.getObject("data", LocalDate.class));
        o.setDescricao(rs.getString("descricao"));
        o.setTipoOcorrencia(rs.getString("tipo_ocorrencia"));
        return o;
    }

    @Override
    public Optional<Ocorrencia> findById(Integer id) {
        String sql = "SELECT * FROM ocorrencia WHERE id_ocorrencia = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar ocorrencia.", e);
        }
    }

    @Override
    public List<Ocorrencia> findAll() {
        String sql = "SELECT * FROM ocorrencia";
        List<Ocorrencia> lista = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapRow(rs));
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar ocorrencias.", e);
        }
    }

    @Override
    public void insert(Ocorrencia o) {
        String sql = "INSERT INTO ocorrencia " +
                "(id_funcionarios, data, descricao, tipo_ocorrencia) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, o.getFuncionario().getId());
            ps.setObject(2, o.getData());
            ps.setString(3, o.getDescricao());
            ps.setString(4, o.getTipoOcorrencia());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                Ocorrencia.class.getSuperclass()
                        .getDeclaredMethod("setId", Integer.class)
                        .invoke(o, keys.getInt(1));
            }
        } catch (Exception e) {
            throw new PersistenceException("Erro ao inserir ocorrencia.", e);
        }
    }

    @Override
    public void update(Ocorrencia o) {
        String sql = "UPDATE ocorrencia SET " +
                "id_funcionarios=?, data=?, descricao=?, tipo_ocorrencia=? " +
                "WHERE id_ocorrencia=?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, o.getFuncionario().getId());
            ps.setObject(2, o.getData());
            ps.setString(3, o.getDescricao());
            ps.setString(4, o.getTipoOcorrencia());
            ps.setInt(5, o.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar ocorrencia.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM ocorrencia WHERE id_ocorrencia = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao excluir ocorrencia.", e);
        }
    }
}
