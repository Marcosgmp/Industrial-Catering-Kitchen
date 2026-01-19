package ui;

import dao.ConsomeDAO;
import dao.FuncionarioClienteDAO;
import dao.RefeicaoDAO;
import factory.DAOFactory;
import model.Consumo;
import model.FuncionarioCliente;
import model.Refeicao;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsumoUI {

    private final Scanner scanner = new Scanner(System.in);
    private final ConsomeDAO consomeDAO;
    private final FuncionarioClienteDAO funcionarioClienteDAO;
    private final RefeicaoDAO refeicaoDAO;

    public ConsumoUI(DAOFactory factory) {
        this.consomeDAO = factory.getConsomeDAO();
        this.funcionarioClienteDAO = factory.getFuncionarioClienteDAO();
        this.refeicaoDAO = factory.getRefeicaoDAO();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("===== MENU CONSUMOS (CONSOME) =====");
            System.out.println("1 - Registrar consumo");
            System.out.println("2 - Listar consumos");
            System.out.println("3 - Buscar consumo por ID");
            System.out.println("4 - Atualizar consumo");
            System.out.println("5 - Excluir consumo");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1 -> criarConsumo();
                case 2 -> listarConsumos();
                case 3 -> buscarConsumoPorId();
                case 4 -> atualizarConsumo();
                case 5 -> excluirConsumo();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }


    private void criarConsumo() {
        System.out.println("--- Registrar Consumo ---");

        int idFunc = lerInt("ID do funcionário do cliente: ");
        Optional<FuncionarioCliente> optFunc = funcionarioClienteDAO.findById(idFunc);
        if (optFunc.isEmpty()) {
            System.out.println("Funcionário do cliente não encontrado.");
            return;
        }

        int idRef = lerInt("ID da refeição: ");
        Optional<Refeicao> optRef = refeicaoDAO.findById(idRef);
        if (optRef.isEmpty()) {
            System.out.println("Refeição não encontrada.");
            return;
        }

        Consumo c = new Consumo();
        c.setFuncionario(optFunc.get());
        c.setRefeicao(optRef.get());

        consomeDAO.insert(c);
        System.out.println("Consumo registrado com ID: " + c.getId());
    }

    private void listarConsumos() {
        System.out.println("--- Lista de Consumos ---");
        List<Consumo> lista = consomeDAO.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nenhum consumo registrado.");
            return;
        }
        for (Consumo c : lista) {
            System.out.printf("%d - FuncionarioCliente %d, Refeicao %d%n",
                    c.getId(),
                    c.getFuncionario() != null ? c.getFuncionario().getId() : null,
                    c.getRefeicao() != null ? c.getRefeicao().getId() : null);
        }
    }

    private void buscarConsumoPorId() {
        System.out.println("--- Buscar Consumo ---");
        int id = lerInt("ID do consumo: ");
        Optional<Consumo> opt = consomeDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Consumo não encontrado.");
            return;
        }
        Consumo c = opt.get();
        System.out.println("ID: " + c.getId());
        System.out.println("ID FuncionárioCliente: " +
                (c.getFuncionario() != null ? c.getFuncionario().getId() : null));
        System.out.println("ID Refeição: " +
                (c.getRefeicao() != null ? c.getRefeicao().getId() : null));
    }

    private void atualizarConsumo() {
        System.out.println("--- Atualizar Consumo ---");
        int id = lerInt("ID do consumo: ");
        Optional<Consumo> opt = consomeDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Consumo não encontrado.");
            return;
        }

        Consumo c = opt.get();
        System.out.println("Deixe em branco para manter o valor atual.");

        String idFuncStr = lerStringOpcional("ID FuncionárioCliente (" +
                (c.getFuncionario() != null ? c.getFuncionario().getId() : "nenhum") + "): ");
        if (!idFuncStr.isBlank()) {
            int novoIdFunc = Integer.parseInt(idFuncStr.trim());
            Optional<FuncionarioCliente> optFunc = funcionarioClienteDAO.findById(novoIdFunc);
            if (optFunc.isEmpty()) {
                System.out.println("Novo funcionário do cliente não encontrado. Mantendo o antigo.");
            } else {
                c.setFuncionario(optFunc.get());
            }
        }

        String idRefStr = lerStringOpcional("ID Refeição (" +
                (c.getRefeicao() != null ? c.getRefeicao().getId() : "nenhuma") + "): ");
        if (!idRefStr.isBlank()) {
            int novoIdRef = Integer.parseInt(idRefStr.trim());
            Optional<Refeicao> optRef = refeicaoDAO.findById(novoIdRef);
            if (optRef.isEmpty()) {
                System.out.println("Nova refeição não encontrada. Mantendo a antiga.");
            } else {
                c.setRefeicao(optRef.get());
            }
        }

        consomeDAO.update(c);
        System.out.println("Consumo atualizado com sucesso.");
    }

    private void excluirConsumo() {
        System.out.println("--- Excluir Consumo ---");
        int id = lerInt("ID do consumo: ");
        consomeDAO.delete(id);
        System.out.println("Consumo excluído (se existia).");
    }


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

    private String lerStringOpcional(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }
}