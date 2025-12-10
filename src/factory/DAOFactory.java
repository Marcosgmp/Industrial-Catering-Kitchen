package factory;

import dao.EmpresaClienteDAO;
import dao.FuncionarioClienteDAO;
import dao.ContratoDAO;
import dao.RefeicaoDAO;
import dao.ConsomeDAO;
import dao.FuncionarioDAO;
import dao.OcorrenciaDAO;
import dao.ProduzDAO;

// Abstração + polimorfismo: cliente usa apenas esta interface
public abstract class DAOFactory {

    public static final int POSTGRES = 1;

    public abstract EmpresaClienteDAO getEmpresaClienteDAO();
    public abstract FuncionarioClienteDAO getFuncionarioClienteDAO();
    public abstract ContratoDAO getContratoDAO();
    public abstract RefeicaoDAO getRefeicaoDAO();
    public abstract ConsomeDAO getConsomeDAO();
    public abstract FuncionarioDAO getFuncionarioDAO();
    public abstract OcorrenciaDAO getOcorrenciaDAO();
    public abstract ProduzDAO getProduzDAO();

    public static DAOFactory getFactory(int tipo) {
        switch (tipo) {
            case POSTGRES:
                return new PostgresDAOFactory();
            default:
                throw new IllegalArgumentException("Tipo de fábrica inválido: " + tipo);
        }
    }
}
