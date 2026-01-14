package ui;

import dao.ContratoDAO;
import dao.EmpresaClienteDAO;
import dao.FuncionarioDAO;
import factory.DAOFactory;
import model.EmpresaCliente;
import model.Contrato;
import java.time.LocalDate;
import model.Funcionario;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final EmpresaClienteDAO empresaDAO;
    private final FuncionarioDAO funcionarioDAO;
    private final ContratoDAO contratoDAO;


    public ConsoleUI(DAOFactory factory) {
        this.empresaDAO = factory.getEmpresaClienteDAO();
        this.funcionarioDAO = factory.getFuncionarioDAO();
        this.contratoDAO = factory.getContratoDAO();
    }

    public void executar() {
        int opcao;
        do {
            mostrarMenuPrincipal();
            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> menuEmpresas();
                case 2 -> menuFuncionarios();
                case 3 -> menuContratos();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 0);
    }

    //  Menus

    private void mostrarMenuPrincipal() {
        System.out.println("===== MENU PRINCIPAL =====");
        System.out.println("1 - Gerenciar Empresas");
        System.out.println("2 - Gerenciar Funcionários");
        System.out.println("3 - Gerenciar Contratos");
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

    private void menuContratos() {
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

    //  EMPRESA
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


    // CONTRATO
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



    // FUNCIONARIO

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
