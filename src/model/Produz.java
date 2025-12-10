package model;

public class Produz {

    private String idProd;
    private Refeicao refeicao;
    private Funcionario funcionario;

    public Produz() {}

    public String getIdProd() { return idProd; }
    public void setIdProd(String idProd) { this.idProd = idProd; }

    public Refeicao getRefeicao() { return refeicao; }
    public void setRefeicao(Refeicao refeicao) { this.refeicao = refeicao; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
}