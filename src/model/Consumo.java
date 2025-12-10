package model;

public class Consumo extends EntidadeBase {

    private FuncionarioCliente funcionario;
    private Refeicao refeicao;

    public Consumo() {}

    public FuncionarioCliente getFuncionario() { return funcionario; }
    public void setFuncionario(FuncionarioCliente funcionario) { this.funcionario = funcionario; }

    public Refeicao getRefeicao() { return refeicao; }
    public void setRefeicao(Refeicao refeicao) { this.refeicao = refeicao; }
}