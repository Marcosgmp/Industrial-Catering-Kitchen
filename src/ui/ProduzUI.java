package ui;

import dao.FuncionarioDAO;
import dao.ProduzDAO;
import dao.RefeicaoDAO;
import factory.DAOFactory;
import model.Funcionario;
import model.Produz;
import model.Refeicao;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProduzUI {

    private final Scanner scanner = new Scanner(System.in);
    private final ProduzDAO produzDAO;
    private final RefeicaoDAO refeicaoDAO;
    private final FuncionarioDAO funcionarioDAO;

    public ProduzUI(DAOFactory factory) {
        this.produzDAO = factory.getProduzDAO();
        this.refeicaoDAO = factory.getRefeicaoDAO();
        this.funcionarioDAO = factory.getFuncionarioDAO();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("===== MENU PRODUÇÃO (PRODUZ) =====");
            System.out.println("1 - Registrar produção");
            System.out.println("2 - Listar produções");
            System.out.println("3 - Buscar produção por chave");
            System.out.println("4 - Excluir produção");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1 -> criarProduz();
                case 2 -> listarProduz();
                case 3 -> buscarProduzPorChave();
                case 4 -> excluirProduz();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    // ===== CRUD PRODUZ =====
    // (não há update de chave composta; se precisar, apaga e insere de novo)

    private void criarProduz() {
        System.out.println("--- Registrar Produção ---");

        String idProd = lerString("ID do produto (ID_PROD): ");

        int idRef = lerInt("ID da refeição: ");
        Optional<Refeicao> optRef = refeicaoDAO.findById(idRef);
        if (optRef.isEmpty()) {
            System.out.println("Refeição não encontrada.");
            return;
        }

        String cpf = lerString("CPF do funcionário (apenas números ou com máscara): ");
        cpf = cpf.replaceAll("\\D", "");
        Optional<Funcionario> optFunc =
                ((dao.postgresql.PostgresFuncionarioDAO) funcionarioDAO).findByCpf(cpf);
        if (optFunc.isEmpty()) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        Produz p = new Produz();
        p.setIdProd(idProd);
        p.setRefeicao(optRef.get());
        p.setFuncionario(optFunc.get());

        produzDAO.insert(p);
        System.out.println("Produção registrada para produto " + idProd +
                ", refeição " + idRef + ", funcionário " + cpf + ".");
    }

    private void listarProduz() {
        System.out.println("--- Lista de Produções ---");
        List<Produz> lista = produzDAO.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma produção registrada.");
            return;
        }
        for (Produz p : lista) {
            String idProd = p.getIdProd();
            Integer idRef = p.getRefeicao() != null ? p.getRefeicao().getId() : null;
            String cpf = p.getFuncionario() != null ? p.getFuncionario().getCpf() : null;
            System.out.printf("Produto %s - Refeição %s - Funcionário %s%n",
                    idProd, idRef, cpf);
        }
    }

    private void buscarProduzPorChave() {
        System.out.println("--- Buscar Produção ---");

        String idProd = lerString("ID do produto: ");
        int idRef = lerInt("ID da refeição: ");
        String cpf = lerString("CPF do funcionário: ");
        cpf = cpf.replaceAll("\\D", "");

        Optional<Produz> opt =
                ((dao.postgresql.PostgresProduzDAO) produzDAO)
                        .findByChaveComposta(idProd, idRef, cpf);
        if (opt.isEmpty()) {
            System.out.println("Produção não encontrada.");
            return;
        }
        Produz p = opt.get();
        System.out.println("Produto: " + p.getIdProd());
        System.out.println("ID Refeição: " +
                (p.getRefeicao() != null ? p.getRefeicao().getId() : null));
        System.out.println("CPF Funcionário: " +
                (p.getFuncionario() != null ? p.getFuncionario().getCpf() : null));
    }

    private void excluirProduz() {
        System.out.println("--- Excluir Produção ---");

        String idProd = lerString("ID do produto: ");
        int idRef = lerInt("ID da refeição: ");
        String cpf = lerString("CPF do funcionário: ");
        cpf = cpf.replaceAll("\\D", "");

        ((dao.postgresql.PostgresProduzDAO) produzDAO)
                .deleteByChaveComposta(idProd, idRef, cpf);
        System.out.println("Produção excluída (se existia).");
    }

    // ===== Utilitários =====

    private int lerInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                String linha = scanner.nextLine();
                return Integer.parseInt(linha.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, tente novamente.");
            }
        }
    }

    private String lerString(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }
}
