# 🍽️ Industrial Catering Kitchen
### Sistema de Gestão de Cozinha Industrial / Refeições Corporativas

Sistema desenvolvido em **Java**, utilizando **JDBC**, **PostgreSQL** e o **padrão DAO**, com interface em **modo console**, voltado para o gerenciamento completo de refeições corporativas.

---

## 📌 Visão Geral
O **Industrial Catering Kitchen** é um sistema de gerenciamento de cozinha industrial que permite o controle de empresas clientes, contratos, funcionários, refeições, consumo, produção e ocorrências.

O projeto foi desenvolvido com foco em **boas práticas de programação**, **organização em camadas** e **persistência de dados**, sendo ideal para fins acadêmicos e estudo de arquitetura de software em Java.

---

## ⚙️ Tecnologias Utilizadas
- ☕ **Java (JDK 8+)**
- 🗄️ **PostgreSQL**
- 🔌 **JDBC**
- 🧩 **Padrão DAO**
- 🏗️ **Arquitetura em Camadas**
- 🖥️ **Interface Console**

---

## ✨ Funcionalidades
- CRUD completo para todas as entidades do sistema
- Interface de menus em console
- Separação clara entre regras de negócio e persistência
- Implementação do padrão **DAO + Factory**
- Tratamento de exceções personalizadas
- Validação de dados de entrada
- Suporte a **chaves compostas** (ex: entidade `Produz`)
- Código modular e extensível

---

## 🏗️ Arquitetura do Sistema

```text
ConsoleUI
   ↓
DAOFactory
   ↓
[EmpresaDAO | FuncionarioDAO | ContratoDAO | RefeicaoDAO | ...]
   ↓
Postgres...DAO
   ↓
ConnectionFactory
   ↓
PostgreSQL
```

## 📦 Estrutura do Projeto
```text
src/
├── dao/
│   ├── EmpresaClienteDAO.java
│   ├── FuncionarioDAO.java
│   ├── ContratoDAO.java
│   ├── RefeicaoDAO.java
│   ├── ConsumoDAO.java
│   └── OcorrenciaDAO.java
│
├── dao/postgresql/
│   ├── PostgresEmpresaClienteDAO.java
│   ├── PostgresFuncionarioDAO.java
│   ├── PostgresContratoDAO.java
│   ├── PostgresRefeicaoDAO.java
│   └── PostgresConsumoDAO.java
│
├── factory/
│   ├── DAOFactory.java
│   └── PostgresDAOFactory.java
│
├── model/
│   ├── EntidadeBase.java
│   ├── EmpresaCliente.java
│   ├── Funcionario.java
│   ├── FuncionarioCliente.java
│   ├── Contrato.java
│   ├── Refeicao.java
│   ├── Consumo.java
│   ├── Ocorrencia.java
│   └── Produz.java
│
├── ui/
│   ├── ConsoleUI.java
│   ├── EmpresaClienteUI.java
│   ├── FuncionarioUI.java
│   ├── ContratoUI.java
│   ├── RefeicaoUI.java
│   ├── ConsumoUI.java
│   └── OcorrenciaUI.java
│
└── util/
    ├── ConnectionFactory.java
    └── PersistenceException.jav
```

##  Como Executar

Configure o banco de dados PostgreSQL

Ajuste a conexão JDBC

Execute a classe Main.java

Utilize os menus da interface console

## 👨‍💻 Autor
Marcos Gustavo  
Projeto acadêmico desenvolvido para estudo de Java, JDBC e padrão DAO.
