package ui;

import dao.ContratoDAO;
import dao.EmpresaClienteDAO;
import model.Contrato;
import model.EmpresaCliente;  // ← ADICIONADO
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ContratoUI {

    private final Scanner scanner;
    private final ContratoDAO contratoDAO;
    private final EmpresaClienteDAO empresaDAO;

    public ContratoUI(Scanner scanner, ContratoDAO contratoDAO, EmpresaClienteDAO empresaDAO) {
        this.scanner = scanner;
        this.contratoDAO = contratoDAO;
        this.empresaDAO = empresaDAO;
    }

    // MUDANÇA PRINCIPAL: menu() ao invés de menuContratos()
    public void menu() {  // ← CORRIGIDO
        int opcao;
        do {
            System.out.println("===== MENU CONTRATOS =====");
            System.out.println("1 - Cadastrar contrato");
            System.out.println("2 - Listar contratos");
            System.out.println("3 - Buscar contrato por ID");
            System.out.println("4 - Atualizar contrato");
            System.out.println("5 - Excluir contrato");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1 -> criarContrato();
                case 2 -> listarContratos();
                case 3 -> buscarContratoPorId();
                case 4 -> atualizarContrato();
                case 5 -> excluirContrato();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    private void criarContrato() {
        System.out.println("--- Cadastro de Contrato ---");

        int idEmpresa = lerInt("ID da empresa cliente: ");
        var optEmpresa = empresaDAO.findById(idEmpresa);
        if (optEmpresa.isEmpty()) {
            System.out.println("Empresa não encontrada. Cadastre a empresa antes.");
            return;
        }

        Contrato c = new Contrato();
        c.setEmpresa(optEmpresa.get());

        String dataInicioStr = lerString("Data início (AAAA-MM-DD): ");
        String dataFimStr    = lerString("Data fim    (AAAA-MM-DD): ");
        c.setDataInicio(LocalDate.parse(dataInicioStr));
        c.setDataFim(LocalDate.parse(dataFimStr));

        c.setTipo(lerString("Tipo de contrato: "));
        c.setValorMensal(Double.parseDouble(lerString("Valor mensal: ")));
        c.setQtdRefeicao(lerInt("Quantidade de refeições: "));

        contratoDAO.insert(c);
        System.out.println("Contrato cadastrado com ID: " + c.getId());
    }

    private void listarContratos() {
        System.out.println("--- Lista de Contratos ---");
        List<Contrato> lista = contratoDAO.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nenhum contrato cadastrado.");
            return;
        }
        for (Contrato c : lista) {
            System.out.printf("%d - Empresa %d, %s até %s, tipo %s, valor %.2f, qtd %d%n",
                    c.getId(),
                    c.getEmpresa().getId(),
                    c.getDataInicio(),
                    c.getDataFim(),
                    c.getTipo(),
                    c.getValorMensal(),
                    c.getQtdRefeicao());
        }
    }

    private void buscarContratoPorId() {
        System.out.println("--- Buscar Contrato ---");
        int id = lerInt("ID do contrato: ");
        var opt = contratoDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Contrato não encontrado.");
            return;
        }
        Contrato c = opt.get();
        System.out.println("ID: " + c.getId());
        System.out.println("Empresa ID: " + c.getEmpresa().getId());
        System.out.println("Data início: " + c.getDataInicio());
        System.out.println("Data fim: " + c.getDataFim());
        System.out.println("Tipo: " + c.getTipo());
        System.out.println("Valor mensal: " + c.getValorMensal());
        System.out.println("Qtd refeições: " + c.getQtdRefeicao());
    }

    private void atualizarContrato() {
        System.out.println("--- Atualizar Contrato ---");
        int id = lerInt("ID do contrato: ");
        var opt = contratoDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Contrato não encontrado.");
            return;
        }

        Contrato c = opt.get();
        System.out.println("Deixe em branco para manter o valor atual.");

        String idEmpStr = lerStringOpcional("ID empresa (" + c.getEmpresa().getId() + "): ");
        if (!idEmpStr.isBlank()) {
            int idEmpresaNovo = Integer.parseInt(idEmpStr.trim());
            var optEmp = empresaDAO.findById(idEmpresaNovo);
            if (optEmp.isEmpty()) {
                System.out.println("Nova empresa não encontrada. Mantendo a antiga.");
            } else {
                c.setEmpresa(optEmp.get());
            }
        }

        String di = lerStringOpcional("Data início (" + c.getDataInicio() + "): ");
        if (!di.isBlank()) c.setDataInicio(LocalDate.parse(di));

        String df = lerStringOpcional("Data fim (" + c.getDataFim() + "): ");
        if (!df.isBlank()) c.setDataFim(LocalDate.parse(df));

        String tipo = lerStringOpcional("Tipo (" + c.getTipo() + "): ");
        if (!tipo.isBlank()) c.setTipo(tipo);

        String valStr = lerStringOpcional("Valor mensal (" + c.getValorMensal() + "): ");
        if (!valStr.isBlank()) {
            try {
                c.setValorMensal(Double.parseDouble(valStr.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, mantendo.");
            }
        }

        String qtdStr = lerStringOpcional("Qtd refeições (" + c.getQtdRefeicao() + "): ");
        if (!qtdStr.isBlank()) {
            try {
                c.setQtdRefeicao(Integer.parseInt(qtdStr.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Quantidade inválida, mantendo.");
            }
        }

        contratoDAO.update(c);
        System.out.println("Contrato atualizado com sucesso.");
    }

    private void excluirContrato() {
        System.out.println("--- Excluir Contrato ---");
        int id = lerInt("ID do contrato: ");
        contratoDAO.delete(id);
        System.out.println("Contrato excluído (se existia).");
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
