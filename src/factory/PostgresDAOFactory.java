package factory;

import dao.*;
import dao.postgresql.PostgresEmpresaClienteDAO;
import dao.postgresql.PostgresFuncionarioClienteDAO;
import dao.postgresql.PostgresContratoDAO;
import dao.postgresql.PostgresRefeicaoDAO;
import dao.postgresql.PostgresConsomeDAO;
import dao.postgresql.PostgresFuncionarioDAO;
import dao.postgresql.PostgresOcorrenciaDAO;
import dao.postgresql.PostgresProduzDAO;
import util.ConnectionFactory;

public class PostgresDAOFactory extends DAOFactory {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    @Override
    public EmpresaClienteDAO getEmpresaClienteDAO() {
        return new PostgresEmpresaClienteDAO(connectionFactory);
    }

    @Override
    public FuncionarioClienteDAO getFuncionarioClienteDAO() {
        return new PostgresFuncionarioClienteDAO(connectionFactory);
    }

    @Override
    public ContratoDAO getContratoDAO() {
        return new PostgresContratoDAO(connectionFactory);
    }

    @Override
    public RefeicaoDAO getRefeicaoDAO() {
        return new PostgresRefeicaoDAO(connectionFactory);
    }

    @Override
    public ConsomeDAO getConsomeDAO() {
        return new PostgresConsomeDAO(connectionFactory);
    }

    @Override
    public FuncionarioDAO getFuncionarioDAO() {
        return new PostgresFuncionarioDAO(connectionFactory);  // <-- aqui
    }

    @Override
    public OcorrenciaDAO getOcorrenciaDAO() {
        return new PostgresOcorrenciaDAO(connectionFactory);
    }

    @Override
    public ProduzDAO getProduzDAO() {
        return new PostgresProduzDAO(connectionFactory);
    }
}
