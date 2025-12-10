package model;

import java.time.LocalDate;

public class Contrato extends EntidadeBase {

    private EmpresaCliente empresa;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String tipo;
    private Double valorMensal;
    private Integer qtdRefeicao;

    public Contrato() {}

    public EmpresaCliente getEmpresa() { return empresa; }
    public void setEmpresa(EmpresaCliente empresa) { this.empresa = empresa; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Double getValorMensal() { return valorMensal; }
    public void setValorMensal(Double valorMensal) { this.valorMensal = valorMensal; }

    public Integer getQtdRefeicao() { return qtdRefeicao; }
    public void setQtdRefeicao(Integer qtdRefeicao) { this.qtdRefeicao = qtdRefeicao; }
}
