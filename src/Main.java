import dao.EmpresaClienteDAO;
import factory.DAOFactory;
import model.EmpresaCliente;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        DAOFactory factory = DAOFactory.getFactory(DAOFactory.POSTGRES);
        EmpresaClienteDAO empresaDAO = factory.getEmpresaClienteDAO();

        int opcao;
        do {
            mostrarMenu();
            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> criarEmpresa(empresaDAO);
                case 2 -> listarEmpresas(empresaDAO);
                case 3 -> buscarEmpresaPorId(empresaDAO);
                case 4 -> atualizarEmpresa(empresaDAO);
                case 5 -> excluirEmpresa(empresaDAO);
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }

            System.out.println();

        } while (opcao != 0);

        scanner.close();
    }

    // ===== Menu =====

    private static void mostrarMenu() {
        System.out.println("===== MENU EMPRESA CLIENTE =====");
        System.out.println("1 - Cadastrar empresa");
        System.out.println("2 - Listar empresas");
        System.out.println("3 - Buscar empresa por ID");
        System.out.println("4 - Atualizar empresa");
        System.out.println("5 - Excluir empresa");
        System.out.println("0 - Sair");
    }

    // ===== Operações CRUD =====

    private static void criarEmpresa(EmpresaClienteDAO dao) {
        System.out.println("--- Cadastro de Empresa ---");
        EmpresaCliente e = new EmpresaCliente();

        e.setNome(lerString("Nome: "));
        e.setEndereco(lerString("Endereço: "));
        e.setCidade(lerString("Cidade: "));
        e.setEstado(lerString("Estado: "));
        e.setTelefone(lerInt("Telefone (apenas números): "));
        e.setResponsavel(lerString("Responsável: "));
        e.setCnpj(lerString("CNPJ (apenas números): "));

        dao.insert(e);
        System.out.println("Empresa cadastrada com ID: " + e.getId());
    }

    private static void listarEmpresas(EmpresaClienteDAO dao) {
        System.out.println("--- Lista de Empresas ---");
        List<EmpresaCliente> lista = dao.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma empresa cadastrada.");
            return;
        }
        for (EmpresaCliente e : lista) {
            System.out.printf("%d - %s (%s - %s)%n",
                    e.getId(), e.getNome(), e.getCidade(), e.getEstado());
        }
    }

    private static void buscarEmpresaPorId(EmpresaClienteDAO dao) {
        System.out.println("--- Buscar Empresa ---");
        int id = lerInt("ID da empresa: ");
        Optional<EmpresaCliente> opt = dao.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Empresa não encontrada.");
        } else {
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
    }

    private static void atualizarEmpresa(EmpresaClienteDAO dao) {
        System.out.println("--- Atualizar Empresa ---");
        int id = lerInt("ID da empresa: ");
        Optional<EmpresaCliente> opt = dao.findById(id);
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

        dao.update(e);
        System.out.println("Empresa atualizada com sucesso.");
    }

    private static void excluirEmpresa(EmpresaClienteDAO dao) {
        System.out.println("--- Excluir Empresa ---");
        int id = lerInt("ID da empresa: ");
        dao.delete(id);
        System.out.println("Empresa excluída (se existia).");
    }

    // ===== Utilitários de leitura =====

    private static int lerInt(String msg) {
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

    private static String lerString(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    private static String lerStringOpcional(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }
}