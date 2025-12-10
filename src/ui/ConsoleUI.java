package ui;

import dao.EmpresaClienteDAO;
import dao.FuncionarioDAO;
import factory.DAOFactory;
import model.EmpresaCliente;
import model.Funcionario;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final EmpresaClienteDAO empresaDAO;
    private final FuncionarioDAO funcionarioDAO;

    public ConsoleUI(DAOFactory factory) {
        this.empresaDAO = factory.getEmpresaClienteDAO();
        this.funcionarioDAO = factory.getFuncionarioDAO();
    }

    public void executar() {
        int opcao;
        do {
            mostrarMenuPrincipal();
            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> menuEmpresas();
                case 2 -> menuFuncionarios();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    // ===== Menus =====

    private void mostrarMenuPrincipal() {
        System.out.println("===== MENU PRINCIPAL =====");
        System.out.println("1 - Gerenciar Empresas");
        System.out.println("2 - Gerenciar Funcionários");
        System.out.println("0 - Sair");
    }

    private void menuEmpresas() {
        int opcao;
        do {
            System.out.println("===== MENU EMPRESAS =====");
            System.out.println("1 - Cadastrar empresa");
            System.out.println("2 - Listar empresas");
            System.out.println("3 - Buscar empresa por ID");
            System.out.println("4 - Atualizar empresa");
            System.out.println("5 - Excluir empresa");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1 -> criarEmpresa();
                case 2 -> listarEmpresas();
                case 3 -> buscarEmpresaPorId();
                case 4 -> atualizarEmpresa();
                case 5 -> excluirEmpresa();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    private void menuFuncionarios() {
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

    // ===== CRUD EMPRESA =====

    private void criarEmpresa() {
        System.out.println("--- Cadastro de Empresa ---");
        EmpresaCliente e = new EmpresaCliente();

        e.setNome(lerString("Nome: "));
        e.setEndereco(lerString("Endereço: "));
        e.setCidade(lerString("Cidade: "));
        e.setEstado(lerString("Estado: "));
        e.setTelefone(lerInt("Telefone (apenas números): "));
        e.setResponsavel(lerString("Responsável: "));
        e.setCnpj(lerString("CNPJ (apenas números): "));

        empresaDAO.insert(e);
        System.out.println("Empresa cadastrada com ID: " + e.getId());
    }

    private void listarEmpresas() {
        System.out.println("--- Lista de Empresas ---");
        List<EmpresaCliente> lista = empresaDAO.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma empresa cadastrada.");
            return;
        }
        for (EmpresaCliente e : lista) {
            System.out.printf("%d - %s (%s - %s)%n",
                    e.getId(), e.getNome(), e.getCidade(), e.getEstado());
        }
    }

    private void buscarEmpresaPorId() {
        System.out.println("--- Buscar Empresa ---");
        int id = lerInt("ID da empresa: ");
        Optional<EmpresaCliente> opt = empresaDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Empresa não encontrada.");
            return;
        }
        EmpresaCliente e = opt.get();
        System.out.println("ID: " + e.getId());
        System.out.println("Nome: " + e.getNome());
        System.out.println("Endereço: " + e.getEndereco());
        System.out.println("Cidade: " + e.getCidade());
        System.out.println("Estado: " + e.getEstado());
        System.out.println("Telefone: " + e.getTelefone());
        System.out.println("Responsável: " + e.getResponsavel());
        System.out.println("CNPJ: " + e.getCnpj());
    }

    private void atualizarEmpresa() {
        System.out.println("--- Atualizar Empresa ---");
        int id = lerInt("ID da empresa: ");
        Optional<EmpresaCliente> opt = empresaDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Empresa não encontrada.");
            return;
        }

        EmpresaCliente e = opt.get();
        System.out.println("Deixe em branco para manter o valor atual.");

        String nome = lerStringOpcional("Nome (" + e.getNome() + "): ");
        if (!nome.isBlank()) e.setNome(nome);

        String endereco = lerStringOpcional("Endereço (" + e.getEndereco() + "): ");
        if (!endereco.isBlank()) e.setEndereco(endereco);

        String cidade = lerStringOpcional("Cidade (" + e.getCidade() + "): ");
        if (!cidade.isBlank()) e.setCidade(cidade);

        String estado = lerStringOpcional("Estado (" + e.getEstado() + "): ");
        if (!estado.isBlank()) e.setEstado(estado);

        String telStr = lerStringOpcional("Telefone (" + e.getTelefone() + "): ");
        if (!telStr.isBlank()) {
            try {
                e.setTelefone(Integer.parseInt(telStr.trim()));
            } catch (NumberFormatException ex) {
                System.out.println("Telefone inválido, mantendo valor anterior.");
            }
        }

        String resp = lerStringOpcional("Responsável (" + e.getResponsavel() + "): ");
        if (!resp.isBlank()) e.setResponsavel(resp);

        String cnpj = lerStringOpcional("CNPJ (" + e.getCnpj() + "): ");
        if (!cnpj.isBlank()) e.setCnpj(cnpj);

        empresaDAO.update(e);
        System.out.println("Empresa atualizada com sucesso.");
    }

    private void excluirEmpresa() {
        System.out.println("--- Excluir Empresa ---");
        int id = lerInt("ID da empresa: ");
        empresaDAO.delete(id);
        System.out.println("Empresa excluída (se existia).");
    }

    // ===== CRUD FUNCIONARIO (tabela funcionarios) =====

    private void criarFuncionario() {
        System.out.println("--- Cadastro de Funcionário ---");
        Funcionario f = new Funcionario();

        String cpfDigitado = lerString("CPF (apenas números ou com máscara): ");
        String cpfLimpo = cpfDigitado.replaceAll("\\D", ""); // remove tudo que não é dígito
        f.setCpf(cpfLimpo);
        f.setCargo(lerString("Cargo: "));
        f.setEndereco(lerString("Endereço: "));
        f.setTelefone(lerInt("Telefone (apenas números): "));
        f.setSalario(Double.parseDouble(lerString("Salário: ")));
        // para simplificar, não pedi datas aqui; pode adicionar depois

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
        Optional<Funcionario> opt = ((dao.postgresql.PostgresFuncionarioDAO) funcionarioDAO)
                .findByCpf(cpf);
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

    // ===== Utilitários de leitura =====

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
