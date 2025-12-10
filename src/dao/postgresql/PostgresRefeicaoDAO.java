package dao.postgresql;

import dao.PersistenceException;
import dao.RefeicaoDAO;
import model.Contrato;
import model.Refeicao;
import util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresRefeicaoDAO implements RefeicaoDAO {

    private final ConnectionFactory connectionFactory;

    public PostgresRefeicaoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private Refeicao mapRow(ResultSet rs) throws SQLException {
        Refeicao r = new Refeicao();
        try {
            Refeicao.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(r, rs.getInt("id_refeicao"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear ID de refeicao.", e);
        }
        Contrato c = new Contrato();
        try {
            Contrato.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(c, rs.getInt("id_contrato"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear contrato da refeicao.", e);
        }
        r.setContrato(c);
        r.setHorario(rs.getObject("horario", LocalTime.class));
        r.setData(rs.getObject("data", LocalDate.class));
        r.setObservacao(rs.getString("observacao"));
        r.setDescricaoCardapio(rs.getString("des_cardapio"));
        return r;
    }

    @Override
    public Optional<Refeicao> findById(Integer id) {
        String sql = "SELECT * FROM refeicao WHERE id_refeicao = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar refeição.", e);
        }
    }

    @Override
    public List<Refeicao> findAll() {
        String sql = "SELECT * FROM refeicao";
        List<Refeicao> lista = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar refeições.", e);
        }
    }

    @Override
    public void insert(Refeicao r) {
        String sql = "INSERT INTO refeicao " +
                "(id_contrato, horario, data, observacao, des_cardapio) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getContrato().getId());
            ps.setObject(2, r.getHorario());
            ps.setObject(3, r.getData());
            ps.setString(4, r.getObservacao());
            ps.setString(5, r.getDescricaoCardapio());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                Refeicao.class.getSuperclass()
                        .getDeclaredMethod("setId", Integer.class)
                        .invoke(r, keys.getInt(1));
            }
        } catch (Exception e) {
            throw new PersistenceException("Erro ao inserir refeição.", e);
        }
    }

    @Override
    public void update(Refeicao r) {
        String sql = "UPDATE refeicao SET " +
                "id_contrato=?, horario=?, data=?, observacao=?, des_cardapio=? " +
                "WHERE id_refeicao=?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getContrato().getId());
            ps.setObject(2, r.getHorario());
            ps.setObject(3, r.getData());
            ps.setString(4, r.getObservacao());
            ps.setString(5, r.getDescricaoCardapio());
            ps.setInt(6, r.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar refeição.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM refeicao WHERE id_refeicao = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao excluir refeição.", e);
        }
    }
}