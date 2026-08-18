# ZeroAndar — Projeto Integrador 3 — Etapa 7

Projeto Java da **Etapa 7 do PI3/UC15**, construído **a partir do projeto completo e testado da Etapa 6**.

Nesta etapa o código da Etapa 6 é mantido como base do projeto e são incorporados **testes unitários com JUnit**, além do plano de testes solicitado no enunciado.

## Objetivo

O enunciado da Etapa 7 solicita:

- criar um projeto de testes JUnit;
- incluir ao menos um teste unitário;
- preferir funcionalidades de cálculos simples;
- dispensar testes que dependam diretamente do banco;
- criar uma regra adicional somente se necessário;
- versionar o projeto de testes;
- elaborar um plano de testes básico para os requisitos já implementados e para os requisitos previstos no sistema Web.

## Base utilizada

A estrutura e o código desta etapa foram derivados diretamente do projeto:

**ZeroAndar — PI3 Etapa 6 — versão 1.6.0**

Foram preservados:

- `model`;
- `repository`;
- `repository.jdbc`;
- `service`;
- `infrastructure`;
- `database`;
- `Main.java`;
- código JDBC;
- regras e validações da Etapa 6.

A Etapa 7 não cria uma arquitetura nova nem antecipa as etapas 8 e 9.

## Alteração específica da Etapa 7

Foi acrescentada a dependência JUnit Jupiter e criada a pasta:

```text
src/test/java/uc15/etapa7/zeroandar/test/
```

com a classe:

```text
ZeroAndarTest.java
```

Os testes verificam funcionalidades simples já existentes na Etapa 6:

- cálculo do valor total do imóvel;
- cálculo de comissão do corretor;
- validação de CPF;
- validação de e-mail;
- validação de CEP;
- validação de UF;
- validação de nome;
- validação de telefone.

Os testes não acessam o banco de dados.

## Regra principal testada

O método já existente em `Imovel`:

```java
calcularValorTotal()
```

é utilizado sem alteração.

A regra verificada é:

```text
valor total = valor de venda + valor de IPTU
```

Assim a Etapa 7 atende à orientação do enunciado para priorizar um cálculo simples, sem inventar uma nova regra de negócio.

## Estrutura

```text
ZeroAndar_Etapa7/
├── database/
│   └── zeroandar_db.sql
├── docs/
│   ├── Plano_de_Testes_Etapa7.docx
│   ├── COMANDOS_VERSIONAMENTO.md
│   └── EVIDENCIA_VERSIONAMENTO.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── uc15/etapa7/zeroandar/
│   │   │       ├── infrastructure/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── test/
│   │   └── resources/
│   └── test/
│       └── java/
│           └── uc15/etapa7/zeroandar/test/
│               └── ZeroAndarTest.java
├── .gitignore
├── pom.xml
└── README.md
```

## Execução

Requer:

- Java 25;
- Maven 3.6.19 ou compatível;
- NetBeans 28.

Para executar os testes:

```bash
mvn clean test
```

No NetBeans:

```text
Test
```

ou:

```text
Clean and Test
```

Os testes JUnit não exigem MySQL.

## Resultado esperado

A execução deve apresentar:

```text
Tests run: 10, Failures: 0, Errors: 0
BUILD SUCCESS
```

## Banco

O banco da Etapa 6 foi preservado porque o projeto da Etapa 7 é uma continuação direta da etapa anterior.

Entretanto, **os testes JUnit desta etapa não utilizam o banco**, conforme permitido pelo enunciado.

## Versionamento

O enunciado solicita o versionamento do projeto de testes no repositório criado na etapa anterior.

Os comandos e a orientação para produzir a evidência real estão em:

```text
docs/COMANDOS_VERSIONAMENTO.md
docs/EVIDENCIA_VERSIONAMENTO.md
```

Não foi fabricada uma captura de tela do GitHub. A evidência deverá ser obtida após o commit/push real do projeto.

## Plano de testes

O documento:

```text
docs/Plano_de_Testes_Etapa7.docx
```

contém:

- objetivo;
- escopo;
- estratégia;
- testes unitários da Etapa 7;
- testes manuais previstos para os principais requisitos do sistema Web;
- critérios de entrada;
- critérios de saída;
- evidências.

## Compatibilidade entre as etapas

A Etapa 7 continua o conceito do ZeroAndar, mas permanece um projeto NetBeans separado, conforme definido para o PI3.

A Etapa 8 será responsável pelo front-end Web.

A Etapa 9 será responsável pela aplicação Java Web e integração com banco.


