package dao.postgresql;

import dao.ContratoDAO;
import dao.PersistenceException;
import model.Contrato;
import model.EmpresaCliente;
import util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresContratoDAO implements ContratoDAO {

    private final ConnectionFactory connectionFactory;

    public PostgresContratoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private Contrato mapRow(ResultSet rs) throws SQLException {
        Contrato c = new Contrato();
        try {
            Contrato.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(c, rs.getInt("id_contrato"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear ID de contrato.", e);
        }
        EmpresaCliente emp = new EmpresaCliente();
        try {
            EmpresaCliente.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(emp, rs.getInt("id_empresa"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear empresa do contrato.", e);
        }
        c.setEmpresa(emp);
        c.setDataInicio(rs.getObject("data_inicio", LocalDate.class));
        c.setDataFim(rs.getObject("data_fim", LocalDate.class));
        c.setTipo(rs.getString("tipo"));
        c.setValorMensal(rs.getDouble("valor_mensal"));
        c.setQtdRefeicao(rs.getInt("qtd_refeicao"));
        return c;
    }

    @Override
    public Optional<Contrato> findById(Integer id) {
        String sql = "SELECT * FROM contrato WHERE id_contrato = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar contrato.", e);
        }
    }

    @Override
    public List<Contrato> findAll() {
        String sql = "SELECT * FROM contrato";
        List<Contrato> lista = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar contratos.", e);
        }
    }

    @Override
    public void insert(Contrato c) {
        String sql = "INSERT INTO contrato " +
                "(id_empresa, data_inicio, data_fim, tipo, valor_mensal, qtd_refeicao) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getEmpresa().getId());
            ps.setObject(2, c.getDataInicio());
            ps.setObject(3, c.getDataFim());
            ps.setString(4, c.getTipo());
            ps.setDouble(5, c.getValorMensal());
            ps.setInt(6, c.getQtdRefeicao());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                Contrato.class.getSuperclass()
                        .getDeclaredMethod("setId", Integer.class)
                        .invoke(c, keys.getInt(1));
            }
        } catch (Exception e) {
            throw new PersistenceException("Erro ao inserir contrato.", e);
        }
    }

    @Override
    public void update(Contrato c) {
        String sql = "UPDATE contrato SET " +
                "id_empresa=?, data_inicio=?, data_fim=?, tipo=?, " +
                "valor_mensal=?, qtd_refeicao=? WHERE id_contrato=?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getEmpresa().getId());
            ps.setObject(2, c.getDataInicio());
            ps.setObject(3, c.getDataFim());
            ps.setString(4, c.getTipo());
            ps.setDouble(5, c.getValorMensal());
            ps.setInt(6, c.getQtdRefeicao());
            ps.setInt(7, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar contrato.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM contrato WHERE id_contrato = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao excluir contrato.", e);
        }
    }
}
