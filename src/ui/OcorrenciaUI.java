package ui;

import dao.FuncionarioClienteDAO;
import dao.OcorrenciaDAO;
import factory.DAOFactory;
import model.FuncionarioCliente;
import model.Ocorrencia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class OcorrenciaUI {

    private final Scanner scanner = new Scanner(System.in);
    private final OcorrenciaDAO ocorrenciaDAO;
    private final FuncionarioClienteDAO funcionarioClienteDAO;

    public OcorrenciaUI(DAOFactory factory) {
        this.ocorrenciaDAO = factory.getOcorrenciaDAO();
        this.funcionarioClienteDAO = factory.getFuncionarioClienteDAO();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("===== MENU OCORRÊNCIAS =====");
            System.out.println("1 - Registrar ocorrência");
            System.out.println("2 - Listar ocorrências");
            System.out.println("3 - Buscar ocorrência por ID");
            System.out.println("4 - Atualizar ocorrência");
            System.out.println("5 - Excluir ocorrência");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1 -> criarOcorrencia();
                case 2 -> listarOcorrencias();
                case 3 -> buscarOcorrenciaPorId();
                case 4 -> atualizarOcorrencia();
                case 5 -> excluirOcorrencia();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    // ===== CRUD OCORRÊNCIA =====

    private void criarOcorrencia() {
        System.out.println("--- Registrar Ocorrência ---");

        int idFunc = lerInt("ID do funcionário do cliente: ");
        Optional<FuncionarioCliente> optFunc = funcionarioClienteDAO.findById(idFunc);
        if (optFunc.isEmpty()) {
            System.out.println("Funcionário do cliente não encontrado.");
            return;
        }

        Ocorrencia o = new Ocorrencia();
        o.setFuncionario(optFunc.get());

        String dataStr = lerString("Data (AAAA-MM-DD): ");
        o.setData(LocalDate.parse(dataStr));

        o.setDescricao(lerString("Descrição: "));
        o.setTipoOcorrencia(lerString("Tipo da ocorrência: "));

        ocorrenciaDAO.insert(o);
        System.out.println("Ocorrência registrada com ID: " + o.getId());
    }

    private void listarOcorrencias() {
        System.out.println("--- Lista de Ocorrências ---");
        List<Ocorrencia> lista = ocorrenciaDAO.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma ocorrência registrada.");
            return;
        }
        for (Ocorrencia o : lista) {
            System.out.printf("%d - FuncCliente %d, %s, tipo %s%n",
                    o.getId(),
                    o.getFuncionario() != null ? o.getFuncionario().getId() : null,
                    o.getData(),
                    o.getTipoOcorrencia());
        }
    }

    private void buscarOcorrenciaPorId() {
        System.out.println("--- Buscar Ocorrência ---");
        int id = lerInt("ID da ocorrência: ");
        Optional<Ocorrencia> opt = ocorrenciaDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Ocorrência não encontrada.");
            return;
        }
        Ocorrencia o = opt.get();
        System.out.println("ID: " + o.getId());
        System.out.println("ID FuncionárioCliente: " +
                (o.getFuncionario() != null ? o.getFuncionario().getId() : null));
        System.out.println("Data: " + o.getData());
        System.out.println("Descrição: " + o.getDescricao());
        System.out.println("Tipo: " + o.getTipoOcorrencia());
    }

    private void atualizarOcorrencia() {
        System.out.println("--- Atualizar Ocorrência ---");
        int id = lerInt("ID da ocorrência: ");
        Optional<Ocorrencia> opt = ocorrenciaDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Ocorrência não encontrada.");
            return;
        }

        Ocorrencia o = opt.get();
        System.out.println("Deixe em branco para manter o valor atual.");

        String idFuncStr = lerStringOpcional("ID FuncionárioCliente (" +
                (o.getFuncionario() != null ? o.getFuncionario().getId() : "nenhum") + "): ");
        if (!idFuncStr.isBlank()) {
            int novoIdFunc = Integer.parseInt(idFuncStr.trim());
            Optional<FuncionarioCliente> optFunc = funcionarioClienteDAO.findById(novoIdFunc);
            if (optFunc.isEmpty()) {
                System.out.println("Novo funcionário do cliente não encontrado. Mantendo o antigo.");
            } else {
                o.setFuncionario(optFunc.get());
            }
        }

        String dataStr = lerStringOpcional("Data (" + o.getData() + "): ");
        if (!dataStr.isBlank()) o.setData(LocalDate.parse(dataStr));

        String desc = lerStringOpcional("Descrição (" + o.getDescricao() + "): ");
        if (!desc.isBlank()) o.setDescricao(desc);

        String tipo = lerStringOpcional("Tipo (" + o.getTipoOcorrencia() + "): ");
        if (!tipo.isBlank()) o.setTipoOcorrencia(tipo);

        ocorrenciaDAO.update(o);
        System.out.println("Ocorrência atualizada com sucesso.");
    }

    private void excluirOcorrencia() {
        System.out.println("--- Excluir Ocorrência ---");
        int id = lerInt("ID da ocorrência: ");
        ocorrenciaDAO.delete(id);
        System.out.println("Ocorrência excluída (se existia).");
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

    private String lerStringOpcional(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }
}
