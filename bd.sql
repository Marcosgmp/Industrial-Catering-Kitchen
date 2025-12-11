CREATE TABLE empresa_cliente (
    id_empresa      SERIAL PRIMARY KEY,
    endereco        VARCHAR(50),
    estado          VARCHAR(20),
    cidade          VARCHAR(30),
    telefone        INTEGER,
    nome            VARCHAR(50),
    responsavel     VARCHAR(50),
    cnpj            VARCHAR(14)
);

CREATE TABLE funcionarios_cliente (
    id_funcionarios SERIAL PRIMARY KEY,
    id_empresa      INTEGER NOT NULL,
    nome            VARCHAR(50),
    cargo           VARCHAR(50),
    matricula       VARCHAR(50),
    CONSTRAINT fk_func_emp
        FOREIGN KEY (id_empresa)
        REFERENCES empresa_cliente (id_empresa)
);

CREATE TABLE ocorrencia (
    id_ocorrencia   SERIAL PRIMARY KEY,
    id_funcionarios INTEGER NOT NULL,
    data            DATE,
    descricao       TEXT,
    tipo_ocorrencia TEXT,
    CONSTRAINT fk_ocor_func
        FOREIGN KEY (id_funcionarios)
        REFERENCES funcionarios_cliente (id_funcionarios)
);

CREATE TABLE contrato (
    id_contrato   SERIAL PRIMARY KEY,
    id_empresa    INTEGER NOT NULL,
    data_inicio   DATE,
    data_fim      DATE,
    tipo          VARCHAR(10),
    valor_mensal  REAL,
    qtd_refeicao  INTEGER,
    CONSTRAINT fk_contrato_emp
        FOREIGN KEY (id_empresa)
        REFERENCES empresa_cliente (id_empresa)
);

CREATE TABLE refeicao (
    id_refeicao   SERIAL PRIMARY KEY,
    id_contrato   INTEGER NOT NULL,
    horario       TIME,
    data          DATE,
    observacao    TEXT,
    des_cardapio  TEXT,
    CONSTRAINT fk_refeicao_contrato
        FOREIGN KEY (id_contrato)
        REFERENCES contrato (id_contrato)
);

CREATE TABLE funcionarios (
    cpf           VARCHAR(11) PRIMARY KEY,
    cargo         VARCHAR(20),
    endereco      VARCHAR(50),
    telefone      INTEGER,
    salario       REAL,
    dt_admissao   DATE,
    dt_ult_pgmt   DATE
);

CREATE TABLE produz (
    id_prod      VARCHAR(20) NOT NULL,
    id_refeicao  INTEGER NOT NULL,
    cpf          VARCHAR(11) NOT NULL,
    PRIMARY KEY (id_prod, id_refeicao, cpf),
    CONSTRAINT fk_produz_refeicao
        FOREIGN KEY (id_refeicao)
        REFERENCES refeicao (id_refeicao),
    CONSTRAINT fk_produz_func
        FOREIGN KEY (cpf)
        REFERENCES funcionarios (cpf)
);

CREATE TABLE consome (
    id_consumo       SERIAL PRIMARY KEY,
    id_funcionarios  INTEGER NOT NULL,
    id_refeicao      INTEGER NOT NULL,
    CONSTRAINT fk_cons_func
        FOREIGN KEY (id_funcionarios)
        REFERENCES funcionarios_cliente (id_funcionarios),
    CONSTRAINT fk_cons_refeicao
        FOREIGN KEY (id_refeicao)
        REFERENCES refeicao (id_refeicao)
);
