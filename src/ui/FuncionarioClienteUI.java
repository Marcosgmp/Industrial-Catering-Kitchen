package ui;

import dao.EmpresaClienteDAO;
import dao.FuncionarioClienteDAO;
import factory.DAOFactory;
import model.EmpresaCliente;
import model.FuncionarioCliente;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FuncionarioClienteUI {

    private final Scanner scanner = new Scanner(System.in);
    private final FuncionarioClienteDAO funcionarioClienteDAO;
    private final EmpresaClienteDAO empresaDAO;

    public FuncionarioClienteUI(DAOFactory factory) {
        this.funcionarioClienteDAO = factory.getFuncionarioClienteDAO();
        this.empresaDAO = factory.getEmpresaClienteDAO();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("===== MENU FUNCIONÁRIOS DO CLIENTE =====");
            System.out.println("1 - Cadastrar funcionário do cliente");
            System.out.println("2 - Listar funcionários do cliente");
            System.out.println("3 - Buscar funcionário do cliente por ID");
            System.out.println("4 - Atualizar funcionário do cliente");
            System.out.println("5 - Excluir funcionário do cliente");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1 -> criarFuncionarioCliente();
                case 2 -> listarFuncionariosCliente();
                case 3 -> buscarFuncionarioClientePorId();
                case 4 -> atualizarFuncionarioCliente();
                case 5 -> excluirFuncionarioCliente();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    // ==== CRUD FUNCIONÁRIO_CLIENTE ====

    private void criarFuncionarioCliente() {
        System.out.println("--- Cadastro de Funcionário do Cliente ---");

        int idEmpresa = lerInt("ID da empresa cliente: ");
        Optional<EmpresaCliente> optEmpresa = empresaDAO.findById(idEmpresa);
        if (optEmpresa.isEmpty()) {
            System.out.println("Empresa não encontrada. Cadastre a empresa antes.");
            return;
        }

        FuncionarioCliente f = new FuncionarioCliente();
        f.setEmpresa(optEmpresa.get());

        f.setNome(lerString("Nome: "));
        f.setCargo(lerString("Cargo: "));
        f.setMatricula(lerString("Matrícula: "));

        funcionarioClienteDAO.insert(f);
        System.out.println("Funcionário do cliente cadastrado com ID: " + f.getId());
    }

    private void listarFuncionariosCliente() {
        System.out.println("--- Lista de Funcionários do Cliente ---");
        List<FuncionarioCliente> lista = funcionarioClienteDAO.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nenhum funcionário do cliente cadastrado.");
            return;
        }
        for (FuncionarioCliente f : lista) {
            System.out.printf("%d - %s, cargo %s, empresa %d%n",
                    f.getId(),
                    f.getNome(),
                    f.getCargo(),
                    f.getEmpresa() != null ? f.getEmpresa().getId() : null);
        }
    }

    private void buscarFuncionarioClientePorId() {
        System.out.println("--- Buscar Funcionário do Cliente ---");
        int id = lerInt("ID do funcionário do cliente: ");
        Optional<FuncionarioCliente> opt = funcionarioClienteDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Funcionário do cliente não encontrado.");
            return;
        }
        FuncionarioCliente f = opt.get();
        System.out.println("ID: " + f.getId());
        System.out.println("Empresa ID: " + (f.getEmpresa() != null ? f.getEmpresa().getId() : null));
        System.out.println("Nome: " + f.getNome());
        System.out.println("Cargo: " + f.getCargo());
        System.out.println("Matrícula: " + f.getMatricula());
    }

    private void atualizarFuncionarioCliente() {
        System.out.println("--- Atualizar Funcionário do Cliente ---");
        int id = lerInt("ID do funcionário do cliente: ");
        Optional<FuncionarioCliente> opt = funcionarioClienteDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Funcionário do cliente não encontrado.");
            return;
        }

        FuncionarioCliente f = opt.get();
        System.out.println("Deixe em branco para manter o valor atual.");

        String idEmpStr = lerStringOpcional("ID empresa (" +
                (f.getEmpresa() != null ? f.getEmpresa().getId() : "nenhuma") + "): ");
        if (!idEmpStr.isBlank()) {
            int novoIdEmp = Integer.parseInt(idEmpStr.trim());
            Optional<EmpresaCliente> optEmp = empresaDAO.findById(novoIdEmp);
            if (optEmp.isEmpty()) {
                System.out.println("Nova empresa não encontrada. Mantendo a antiga.");
            } else {
                f.setEmpresa(optEmp.get());
            }
        }

        String nome = lerStringOpcional("Nome (" + f.getNome() + "): ");
        if (!nome.isBlank()) f.setNome(nome);

        String cargo = lerStringOpcional("Cargo (" + f.getCargo() + "): ");
        if (!cargo.isBlank()) f.setCargo(cargo);

        String mat = lerStringOpcional("Matrícula (" + f.getMatricula() + "): ");
        if (!mat.isBlank()) f.setMatricula(mat);

        funcionarioClienteDAO.update(f);
        System.out.println("Funcionário do cliente atualizado com sucesso.");
    }

    private void excluirFuncionarioCliente() {
        System.out.println("--- Excluir Funcionário do Cliente ---");
        int id = lerInt("ID do funcionário do cliente: ");
        funcionarioClienteDAO.delete(id);
        System.out.println("Funcionário do cliente excluído (se existia).");
    }

    // ==== Utilitários ====

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