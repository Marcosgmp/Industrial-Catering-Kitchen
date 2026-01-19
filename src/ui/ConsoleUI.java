package ui;

import factory.DAOFactory;
<<<<<<< HEAD
=======
import model.EmpresaCliente;
import model.Contrato;
import java.time.LocalDate;
import model.Funcionario;
>>>>>>> af59f2874bab788d7ac706c2d42bcd213fd2e219

import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    private final EmpresaClienteUI empresaUI;
    private final FuncionarioUI funcionarioUI;
    private final ContratoUI contratoUI;
    private final RefeicaoUI refeicaoUI;
    private final FuncionarioClienteUI funcionarioClienteUI;
    private final ConsumoUI consumoUI;
    private final OcorrenciaUI ocorrenciaUI;
    private final ProduzUI produzUI;

    public ConsoleUI(DAOFactory factory) {
        this.empresaUI            = new EmpresaClienteUI(scanner, factory.getEmpresaClienteDAO());
        this.funcionarioUI        = new FuncionarioUI(scanner, factory.getFuncionarioDAO());
        this.contratoUI           = new ContratoUI(scanner, factory.getContratoDAO(), factory.getEmpresaClienteDAO());
        this.refeicaoUI           = new RefeicaoUI(factory);
        this.funcionarioClienteUI = new FuncionarioClienteUI(factory);
        this.consumoUI            = new ConsumoUI(factory);
        this.ocorrenciaUI         = new OcorrenciaUI(factory);
        this.produzUI             = new ProduzUI(factory);
    }

    public void executar() {
        int opcao;
        do {
            mostrarMenuPrincipal();
            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> empresaUI.menu();
                case 2 -> funcionarioUI.menu();
                case 3 -> contratoUI.menu();
                case 4 -> refeicaoUI.menu();
                case 5 -> funcionarioClienteUI.menu();
                case 6 -> consumoUI.menu();
                case 7 -> ocorrenciaUI.menu();
                case 8 -> produzUI.menu();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    private void mostrarMenuPrincipal() {
        System.out.println("===== MENU PRINCIPAL =====");
        System.out.println("1 - Gerenciar Empresas");
        System.out.println("2 - Gerenciar Funcionários");
        System.out.println("3 - Gerenciar Contratos");
        System.out.println("4 - Gerenciar Refeições");
        System.out.println("5 - Gerenciar Funcionários do Cliente");
        System.out.println("6 - Gerenciar Consumos");
        System.out.println("7 - Gerenciar Ocorrências");
        System.out.println("8 - Gerenciar Produção ");
        System.out.println("0 - Sair");
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
}
