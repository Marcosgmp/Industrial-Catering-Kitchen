package dao.postgresql;

import dao.PersistenceException;
import dao.ProduzDAO;
import model.Funcionario;
import model.Produz;
import model.Refeicao;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresProduzDAO implements ProduzDAO {

    private final ConnectionFactory connectionFactory;

    public PostgresProduzDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private Produz mapRow(ResultSet rs) throws SQLException {
        Produz p = new Produz();
        p.setIdProd(rs.getString("id_prod"));

        Refeicao r = new Refeicao();
        try {
            Refeicao.class.getSuperclass()
                    .getDeclaredMethod("setId", Integer.class)
                    .invoke(r, rs.getInt("id_refeicao"));
        } catch (Exception e) {
            throw new SQLException("Erro ao mapear refeicao em produz.", e);
        }

        Funcionario f = new Funcionario();
        f.setCpf(rs.getString("cpf"));

        p.setRefeicao(r);
        p.setFuncionario(f);
        return p;
    }

    // Como ProduzDAO estende DAO<Produz>, usaremos o id inteiro apenas como "fake" (não usado).
    @Override
    public Optional<Produz> findById(Integer id) {
        throw new UnsupportedOperationException("Use findByChaveComposta em Produz.");
    }

    public Optional<Produz> findByChaveComposta(String idProd, Integer idRefeicao, String cpf) {
        String sql = "SELECT * FROM produz WHERE id_prod=? AND id_refeicao=? AND cpf=?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idProd);
            ps.setInt(2, idRefeicao);
            ps.setString(3, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao buscar registro em produz.", e);
        }
    }

    @Override
    public List<Produz> findAll() {
        String sql = "SELECT * FROM produz";
        List<Produz> lista = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapRow(rs));
            return lista;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao listar registros de produz.", e);
        }
    }

    @Override
    public void insert(Produz p) {
        String sql = "INSERT INTO produz (id_prod, id_refeicao, cpf) VALUES (?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getIdProd());
            ps.setInt(2, p.getRefeicao().getId());
            ps.setString(3, p.getFuncionario().getCpf());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao inserir registro em produz.", e);
        }
    }

    @Override
    public void update(Produz p) {
        // Em chave composta, normalmente não se atualiza a PK; se precisar, faz delete+insert.
        throw new UnsupportedOperationException("Atualização de chave composta não suportada. Use delete+insert.");
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Use deleteByChaveComposta em Produz.");
    }

    public void deleteByChaveComposta(String idProd, Integer idRefeicao, String cpf) {
        String sql = "DELETE FROM produz WHERE id_prod=? AND id_refeicao=? AND cpf=?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idProd);
            ps.setInt(2, idRefeicao);
            ps.setString(3, cpf);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao excluir registro em produz.", e);
        }
    }
}