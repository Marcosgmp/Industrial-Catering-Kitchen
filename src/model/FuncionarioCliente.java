package model;

public class FuncionarioCliente extends EntidadeBase {

    private EmpresaCliente empresa;
    private String nome;
    private String cargo;
    private String matricula;

    public FuncionarioCliente() {}

    public EmpresaCliente getEmpresa() { return empresa; }
    public void setEmpresa(EmpresaCliente empresa) { this.empresa = empresa; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
}
