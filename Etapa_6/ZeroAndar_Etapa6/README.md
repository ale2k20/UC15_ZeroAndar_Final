# ZeroAndar — UC15 — Projeto Integrador 3 — Etapa 6

Refatoração do sistema desktop ZeroAndar (PI2 / Etapa 4) para preparar o núcleo de domínio e regra de negócio para reutilização em uma futura aplicação Web.

> ⚠️ Esta etapa **não implementa o back-end Web com Spring**. O objetivo é remover o acoplamento com o Java Swing e organizar o código em camadas, deixando o projeto pronto para as Etapas 7 a 9.

## Índice
- [Contexto](#contexto)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Princípios SOLID aplicados](#princípios-solid-aplicados)
- [Padrão de projeto utilizado](#padrão-de-projeto-utilizado)
- [Testes](#testes)
- [Como executar](#como-executar)
- [Banco de dados](#banco-de-dados)

## Contexto
O sistema original (PI2/Etapa 4) foi desenvolvido em Java Swing, com CRUD de Imóveis, Clientes, Proprietários e Corretores, tela de Login e Dashboard, e DAOs concretos em JDBC/MySQL. Nesta etapa, o código foi revisado e refatorado: telas Swing foram eliminadas, e a lógica de negócio antes espalhada entre `view` e `controller` foi reorganizada em camadas independentes de interface gráfica.

## Estrutura do projeto
```
UC15_ZeroAndar_Final/
└── Etapa6/
    ├── src/
    │   ├── model/            → entidades de domínio (Pessoa, PessoaCliente, PessoaProprietario, PessoaCorretor, Imovel, Endereco)
    │   ├── repository/       → contratos de persistência (ClienteDAO, CorretorDAO, ImovelDAO, ProprietarioDAO)
    │   ├── repository/jdbc/  → implementações JDBC/MySQL dos contratos acima
    │   ├── service/          → regras de negócio e validações (ClienteService, ValidacaoService, etc.)
    │   ├── infrastructure/   → configuração de acesso ao banco (antiga DatabaseConnection)
    │   └── test/
    │       └── Main.java     → testes demonstrativos exigidos no enunciado (sem Swing, sem banco)
    ├── docs/
    │   └── UC15_Etapa6_Relatorio.docx
    └── database/
        └── zeroandar_db.sql  → script original do PI2, mantido como referência
```

## Princípios SOLID aplicados
| Princípio | Onde foi aplicado |
|---|---|
| **SRP** | Validações saíram das entidades e foram centralizadas em `ValidacaoService`; acesso a dados saiu dos `services` e ficou isolado nos `repository`; não existe mais View Swing no núcleo refatorado. |
| **DIP** | Os `services` dependem das interfaces `ClienteDAO`, `CorretorDAO`, `ImovelDAO` e `ProprietarioDAO`, nunca das implementações JDBC diretamente. |
| **ISP** | Cada entidade tem seu próprio contrato de persistência, em vez de uma interface genérica única. |
| **OCP** | Novas implementações de persistência (ex.: JPA na Etapa 9) podem ser adicionadas sem alterar o código já existente dos `services`. |

Detalhamento completo, com justificativas por classe, está no relatório em `docs/UC15_Etapa6_Relatorio.docx`.

## Padrão de projeto utilizado
Foi adotado o padrão **Repository**, separando o contrato de persistência (`repository`) das implementações concretas (`repository.jdbc`). Isso prepara a substituição do JDBC por JPA/Spring Data na Etapa 9 sem contaminar o domínio com detalhes de infraestrutura, e permite testar os `services` sem depender de um banco real.

## Testes
Os testes ficam em `test/Main.java` e são executados via `main()`, conforme pedido no enunciado. Foram elaborados inicialmente 34 cenários, mas boa parte dependia de conexão com banco de dados — recurso que esta etapa não exige. Por isso, o conjunto final foi reduzido para **15 testes**, todos independentes de banco (usam um repositório em memória, `ClienteDAOEmMemoria`, para validar a inversão de dependência):

| Grupo | Qtd. | Resultado |
|---|---|---|
| Validações (`ValidacaoService`) | 7 | ✅ 15/15 aprovados |
| Modelos de domínio | 5 | ✅ |
| Inversão de dependência (DIP) | 3 | ✅ |
| **Total** | **15** | **15/15 (100%)** |

```
RESULTADO: 15/15 testes passaram.
BUILD SUCCESS
```

## Como executar
Requer **Java 25** e **Maven**. O projeto foi estruturado como Maven para abrir diretamente no NetBeans 28.

```bash
mvn clean package
java -cp target/zeroandar-1.6.0.jar uc15.etapa6.zeroandar.test.Main
```

Os testes do `Main` usam um repositório em memória e não exigem MySQL. As implementações JDBC ficam preparadas para uso posterior.

## Banco de dados
O script original do PI2 foi preservado em `database/zeroandar_db.sql`, apenas como referência. A senha não fica mais gravada no código: configure as variáveis de ambiente `ZEROANDAR_DB_URL`, `ZEROANDAR_DB_USER` e `ZEROANDAR_DB_PASSWORD` (ou propriedades equivalentes da JVM) quando as implementações JDBC forem utilizadas.

---
**Autor:** Alex · **UC15 — Etapa 6** · Agosto/2026
