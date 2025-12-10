package factory;

import dao.*;
import dao.postgresql.PostgresEmpresaClienteDAO;
import util.ConnectionFactory;

public class PostgresDAOFactory extends DAOFactory {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    @Override
    public EmpresaClienteDAO getEmpresaClienteDAO() {
        return new PostgresEmpresaClienteDAO(connectionFactory);
    }

    @Override
    public FuncionarioClienteDAO getFuncionarioClienteDAO() {
        throw new UnsupportedOperationException("Implementar depois.");
    }

    @Override
    public ContratoDAO getContratoDAO() {
        throw new UnsupportedOperationException("Implementar depois.");
    }

    @Override
    public RefeicaoDAO getRefeicaoDAO() {
        throw new UnsupportedOperationException("Implementar depois.");
    }

    @Override
    public ConsomeDAO getConsomeDAO() {
        throw new UnsupportedOperationException("Implementar depois.");
    }

    @Override
    public FuncionarioDAO getFuncionarioDAO() {
        throw new UnsupportedOperationException("Implementar depois.");
    }

    @Override
    public OcorrenciaDAO getOcorrenciaDAO() {
        throw new UnsupportedOperationException("Implementar depois.");
    }

    @Override
    public ProduzDAO getProduzDAO() {
        throw new UnsupportedOperationException("Implementar depois.");
    }
}
