package dao.postgresql;

import dao.EmpresaClienteDAO;
import dao.PersistenceException;
import model.EmpresaCliente;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresEmpresaClienteDAO implements EmpresaClienteDAO {

    private final ConnectionFactory connectionFactory;

    public PostgresEmpresaClienteDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Optional<EmpresaCliente> findById(Integer id) {
        String sql = "SELECT * FROM empresa_cliente WHERE id_empresa = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar empresa por id.", e);
        }
    }

    @Override
    public List<EmpresaCliente> findAll() {
        String sql = "SELECT * FROM empresa_cliente";
        List<EmpresaCliente> lista = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar empresas.", e);
        }
    }

    @Override
    public void insert(EmpresaCliente e) {
        String sql = "INSERT INTO empresa_cliente " +
                "(endereco, estado, cidade, telefone, nome, responsavel, cnpj) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getEndereco());
            ps.setString(2, e.getEstado());
            ps.setString(3, e.getCidade());
            ps.setObject(4, e.getTelefone(), Types.INTEGER);
            ps.setString(5, e.getNome());
            ps.setString(6, e.getResponsavel());
            ps.setString(7, e.getCnpj());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                // herança + encapsulamento (id na superclasse)
                e.getClass().getSuperclass()
                        .getDeclaredMethod("setId", Integer.class)
                        .invoke(e, keys.getInt(1));
            }
        } catch (Exception ex) {
            throw new PersistenceException("Erro ao inserir empresa.", ex);
        }
    }

    @Override
    public void update(EmpresaCliente e) {
        String sql = "UPDATE empresa_cliente SET " +
                "endereco=?, estado=?, cidade=?, telefone=?, nome=?, responsavel=?, cnpj=? " +
                "WHERE id_empresa=?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getEndereco());
            ps.setString(2, e.getEstado());
            ps.setString(3, e.getCidade());
            ps.setObject(4, e.getTelefone(), Types.INTEGER);
            ps.setString(5, e.getNome());
            ps.setString(6, e.getResponsavel());
            ps.setString(7, e.getCnpj());
            ps.setInt(8, e.getId());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao atualizar empresa.", ex);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM empresa_cliente WHERE id_empresa = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao excluir empresa.", ex);
        }
    }

    private EmpresaCliente mapRow(ResultSet rs) throws SQLException {
        EmpresaCliente e = new EmpresaCliente();
        e.getClass().getSuperclass()
                .getDeclaredMethods();
        e.setEndereco(rs.getString("endereco"));
        e.setEstado(rs.getString("estado"));
        e.setCidade(rs.getString("cidade"));
        e.setTelefone((Integer) rs.getObject("telefone"));
        e.setNome(rs.getString("nome"));
        e.setResponsavel(rs.getString("responsavel"));
        e.setCnpj(rs.getString("cnpj"));
        // acesso direto ao campo id via reflexão pode ser trocado por setter protegido
        try {
            e.getClass().getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(e, rs.getInt("id_empresa"));
        } catch (Exception ex) {
            throw new SQLException("Erro ao mapear ID de empresa.", ex);
        }
        return e;
    }
}
