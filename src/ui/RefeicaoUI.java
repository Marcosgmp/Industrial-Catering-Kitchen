package ui;
import dao.ContratoDAO;
import dao.RefeicaoDAO;
import factory.DAOFactory;
import model.Contrato;
import model.Refeicao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RefeicaoUI {

    private final Scanner scanner = new Scanner(System.in);
    private final RefeicaoDAO refeicaoDAO;
    private final ContratoDAO contratoDAO;

    public RefeicaoUI(DAOFactory factory) {
        this.refeicaoDAO = factory.getRefeicaoDAO();
        this.contratoDAO = factory.getContratoDAO();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("===== MENU REFEIÇÕES =====");
            System.out.println("1 - Cadastrar refeição");
            System.out.println("2 - Listar refeições");
            System.out.println("3 - Buscar refeição por ID");
            System.out.println("4 - Atualizar refeição");
            System.out.println("5 - Excluir refeição");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opção: ");

            switch (opcao) {
                case 1 -> criarRefeicao();
                case 2 -> listarRefeicoes();
                case 3 -> buscarRefeicaoPorId();
                case 4 -> atualizarRefeicao();
                case 5 -> excluirRefeicao();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    // ==== CRUD REFEIÇÃO ====

    private void criarRefeicao() {
        System.out.println("--- Cadastro de Refeição ---");

        int idContrato = lerInt("ID do contrato: ");
        Optional<Contrato> optContrato = contratoDAO.findById(idContrato);
        if (optContrato.isEmpty()) {
            System.out.println("Contrato não encontrado. Cadastre o contrato antes.");
            return;
        }

        Refeicao r = new Refeicao();
        r.setContrato(optContrato.get());

        String dataStr = lerString("Data (AAAA-MM-DD): ");
        String horaStr = lerString("Horário (HH:MM): ");

        r.setData(LocalDate.parse(dataStr));
        r.setHorario(LocalTime.parse(horaStr + ":00")); // simples, adiciona segundos

        r.setObservacao(lerStringOpcional("Observação: "));
        r.setDescricaoCardapio(lerStringOpcional("Descrição do cardápio: "));

        refeicaoDAO.insert(r);
        System.out.println("Refeição cadastrada com ID: " + r.getId());
    }

    private void listarRefeicoes() {
        System.out.println("--- Lista de Refeições ---");
        List<Refeicao> lista = refeicaoDAO.findAll();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma refeição cadastrada.");
            return;
        }
        for (Refeicao r : lista) {
            System.out.printf("%d - Contrato %d, %s %s, %s%n",
                    r.getId(),
                    r.getContrato().getId(),
                    r.getData(),
                    r.getHorario(),
                    r.getDescricaoCardapio());
        }
    }

    private void buscarRefeicaoPorId() {
        System.out.println("--- Buscar Refeição ---");
        int id = lerInt("ID da refeição: ");
        Optional<Refeicao> opt = refeicaoDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Refeição não encontrada.");
            return;
        }
        Refeicao r = opt.get();
        System.out.println("ID: " + r.getId());
        System.out.println("Contrato ID: " + r.getContrato().getId());
        System.out.println("Data: " + r.getData());
        System.out.println("Horário: " + r.getHorario());
        System.out.println("Observação: " + r.getObservacao());
        System.out.println("Cardápio: " + r.getDescricaoCardapio());
    }

    private void atualizarRefeicao() {
        System.out.println("--- Atualizar Refeição ---");
        int id = lerInt("ID da refeição: ");
        Optional<Refeicao> opt = refeicaoDAO.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Refeição não encontrada.");
            return;
        }

        Refeicao r = opt.get();
        System.out.println("Deixe em branco para manter o valor atual.");

        String idContratoStr = lerStringOpcional("ID contrato (" + r.getContrato().getId() + "): ");
        if (!idContratoStr.isBlank()) {
            int novoIdContrato = Integer.parseInt(idContratoStr.trim());
            Optional<Contrato> optContrato = contratoDAO.findById(novoIdContrato);
            if (optContrato.isEmpty()) {
                System.out.println("Novo contrato não encontrado. Mantendo o antigo.");
            } else {
                r.setContrato(optContrato.get());
            }
        }

        String dataStr = lerStringOpcional("Data (" + r.getData() + "): ");
        if (!dataStr.isBlank()) r.setData(LocalDate.parse(dataStr));

        String horaStr = lerStringOpcional("Horário (" + r.getHorario() + "): ");
        if (!horaStr.isBlank()) r.setHorario(LocalTime.parse(horaStr + ":00"));

        String obs = lerStringOpcional("Observação (" + r.getObservacao() + "): ");
        if (!obs.isBlank()) r.setObservacao(obs);

        String card = lerStringOpcional("Cardápio (" + r.getDescricaoCardapio() + "): ");
        if (!card.isBlank()) r.setDescricaoCardapio(card);

        refeicaoDAO.update(r);
        System.out.println("Refeição atualizada com sucesso.");
    }

    private void excluirRefeicao() {
        System.out.println("--- Excluir Refeição ---");
        int id = lerInt("ID da refeição: ");
        refeicaoDAO.delete(id);
        System.out.println("Refeição excluída (se existia).");
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