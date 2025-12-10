package model;

public class EmpresaCliente extends EntidadeBase {

    private String endereco;
    private String estado;
    private String cidade;
    private Integer telefone;
    private String nome;
    private String responsavel;
    private String cnpj;

    public EmpresaCliente() {}

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public Integer getTelefone() { return telefone; }
    public void setTelefone(Integer telefone) { this.telefone = telefone; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
}
