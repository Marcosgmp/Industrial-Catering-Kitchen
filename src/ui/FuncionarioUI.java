package ui;

import dao.FuncionarioDAO;
import model.Funcionario;
import dao.postgresql.PostgresFuncionarioDAO;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FuncionarioUI {

    private final Scanner scanner;
    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioUI(Scanner scanner, FuncionarioDAO funcionarioDAO) {
        this.scanner = scanner;
        this.funcionarioDAO = funcionarioDAO;
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("===== MENU FUNCIONÁRIOS =====");
            System.out.println("1 - Cadastrar funcionário");
            System.out.println("2 - Listar funcionários");
            System.out.println("3 - Buscar funcionário por CPF");
            System.out.println("4 - Atualizar funcionário");
            System.out.println("5 - Excluir funcionário");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1 -> criarFuncionario();
                case 2 -> listarFuncionarios();
                case 3 -> buscarFuncionarioPorCpf();
                case 4 -> atualizarFuncionario();
                case 5 -> excluirFuncionario();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    private void criarFuncionario() {
        System.out.println("--- Cadastro de Funcionário ---");
        Funcionario f = new Funcionario();

        String cpfDigitado = lerString("CPF (apenas números ou com máscara): ");
        String cpfLimpo = cpfDigitado.replaceAll("\\D", "");
        f.setCpf(cpfLimpo);
        f.setCargo(lerString("Cargo: "));
        f.setEndereco(lerString("Endereço: "));
        f.setTelefone(lerInt("Telefone (apenas números): "));
        f.setSalario(Double.parseDouble(lerString("Salário: ")));

        funcionarioDAO.insert(f);
        System.out.println("Funcionário cadastrado com CPF: " + f.getCpf());
    }

    private void listarFuncionarios() {
        System.out.println("--- Lista de Funcionários ---");
        List<Funcionario> lista = funcionarioDAO.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }
        for (Funcionario f : lista) {
            System.out.printf("%s - %s (R$ %.2f)%n",
                    f.getCpf(), f.getCargo(), f.getSalario());
        }
    }

    private void buscarFuncionarioPorCpf() {
        System.out.println("--- Buscar Funcionário ---");
        String cpf = lerString("CPF: ");
        cpf = cpf.replaceAll("\\D", "");
        dao.postgresql.PostgresFuncionarioDAO postgresDAO =
                (dao.postgresql.PostgresFuncionarioDAO) funcionarioDAO;
        Optional<Funcionario> opt = postgresDAO.findByCpf(cpf);

        if (opt.isEmpty()) {
            System.out.println("Funcionário não encontrado.");
            return;
        }
        Funcionario f = opt.get();
        System.out.println("CPF: " + f.getCpf());
        System.out.println("Cargo: " + f.getCargo());
        System.out.println("Endereço: " + f.getEndereco());
        System.out.println("Telefone: " + f.getTelefone());
        System.out.println("Salário: " + f.getSalario());
    }

    private void atualizarFuncionario() {
        System.out.println("--- Atualizar Funcionário ---");
        String cpf = lerString("CPF: ");
        dao.postgresql.PostgresFuncionarioDAO postgresDAO =
                (dao.postgresql.PostgresFuncionarioDAO) funcionarioDAO;

        Optional<Funcionario> opt = postgresDAO.findByCpf(cpf);
        if (opt.isEmpty()) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        Funcionario f = opt.get();
        System.out.println("Deixe em branco para manter o valor atual.");

        String cargo = lerStringOpcional("Cargo (" + f.getCargo() + "): ");
        if (!cargo.isBlank()) f.setCargo(cargo);

        String end = lerStringOpcional("Endereço (" + f.getEndereco() + "): ");
        if (!end.isBlank()) f.setEndereco(end);

        String telStr = lerStringOpcional("Telefone (" + f.getTelefone() + "): ");
        if (!telStr.isBlank()) {
            try {
                f.setTelefone(Integer.parseInt(telStr.trim()));
            } catch (NumberFormatException ex) {
                System.out.println("Telefone inválido, mantendo valor anterior.");
            }
        }

        String salStr = lerStringOpcional("Salário (" + f.getSalario() + "): ");
        if (!salStr.isBlank()) {
            try {
                f.setSalario(Double.parseDouble(salStr.trim()));
            } catch (NumberFormatException ex) {
                System.out.println("Salário inválido, mantendo valor anterior.");
            }
        }

        funcionarioDAO.update(f);
        System.out.println("Funcionário atualizado com sucesso.");
    }

    private void excluirFuncionario() {
        System.out.println("--- Excluir Funcionário ---");
        String cpf = lerString("CPF: ");
        dao.postgresql.PostgresFuncionarioDAO postgresDAO =
                (dao.postgresql.PostgresFuncionarioDAO) funcionarioDAO;
        postgresDAO.deleteByCpf(cpf);
        System.out.println("Funcionário excluído (se existia).");
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

    private String lerString(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    private String lerStringOpcional(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }
}