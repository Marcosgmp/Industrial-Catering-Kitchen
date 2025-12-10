package model;

import java.time.LocalDate;

public class Funcionario {

    private String cpf;
    private String cargo;
    private String endereco;
    private Integer telefone;
    private Double salario;
    private LocalDate dataAdmissao;
    private LocalDate dataUltimoPagamento;

    public Funcionario() {}

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public Integer getTelefone() { return telefone; }
    public void setTelefone(Integer telefone) { this.telefone = telefone; }

    public Double getSalario() { return salario; }
    public void setSalario(Double salario) { this.salario = salario; }

    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public LocalDate getDataUltimoPagamento() { return dataUltimoPagamento; }
    public void setDataUltimoPagamento(LocalDate dataUltimoPagamento) { this.dataUltimoPagamento = dataUltimoPagamento; }
}
