package model;

import java.time.LocalDate;

public class Ocorrencia extends EntidadeBase {

    private FuncionarioCliente funcionario;
    private LocalDate data;
    private String descricao;
    private String tipoOcorrencia;

    public Ocorrencia() {}

    public FuncionarioCliente getFuncionario() { return funcionario; }
    public void setFuncionario(FuncionarioCliente funcionario) { this.funcionario = funcionario; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipoOcorrencia() { return tipoOcorrencia; }
    public void setTipoOcorrencia(String tipoOcorrencia) { this.tipoOcorrencia = tipoOcorrencia; }
}