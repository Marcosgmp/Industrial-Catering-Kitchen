package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Refeicao extends EntidadeBase {

    private Contrato contrato;
    private LocalTime horario;
    private LocalDate data;
    private String observacao;
    private String descricaoCardapio;

    public Refeicao() {}

    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public String getDescricaoCardapio() { return descricaoCardapio; }
    public void setDescricaoCardapio(String descricaoCardapio) { this.descricaoCardapio = descricaoCardapio; }
}
