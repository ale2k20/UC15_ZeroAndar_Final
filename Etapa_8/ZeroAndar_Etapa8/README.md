# ZeroAndar — Projeto Integrador 3 — Etapa 8 (Front-end Web)

Continuação do sistema desktop (Etapas 6 e 7), agora com o **front-end web** do sistema, conforme pedido no enunciado da Etapa 8: páginas em HTML, estilizadas com CSS externo e com validações/dinâmica em JavaScript (jQuery + Bootstrap).

**Sem back-end e sem persistência em banco de dados nesta etapa** — os dados ficam em arrays JavaScript em memória, apenas para ilustrar o funcionamento das telas, facilitando a troca por chamadas ao Spring/banco na Etapa 9.

> **🔑 Login de teste**
> Para acessar o sistema (`login.html`), use:
> - **Usuário:** `alex@zeroandar.com`
> - **Senha:** `senha123`

---

## Estrutura do projeto

```
ZeroAndar_Etapa8/
├── pom.xml                                  # Projeto Maven, com parent spring-boot-starter-parent
├── database/
│   └── zeroandar_db.sql                     # Script da Etapa 7 (referência dos campos/entidades)
├── src/main/java/uc15/etapa8/zeroandar/     # Classes Java trazidas da Etapa 7 (ver seção abaixo)
├── src/test/java/uc15/etapa8/zeroandar/     # Teste JUnit trazido da Etapa 7
└── src/main/resources/static/               # Padrão do Spring Boot para conteúdo estático (classpath:/static/)
    ├── index.html                           # Redireciona para login.html
    ├── login.html
    ├── dashboard.html                       # Painel de abertura do sistema ZeroAndar
    ├── imoveis.html                         # Pesquisa, alteração, inclusão e exclusão de imóveis
    ├── clientes.html
    ├── proprietarios.html                   # Cada arquivo HTML equivale ao que o nome indica
    ├── corretores.html
    ├── css/
    │   ├── style-clientes.css               # Estilo específico da tela de clientes
    │   ├── style-corretores.css             # Estilo específico da tela de corretores
    │   ├── style-dashboard.css              # Estilo específico da tela de dashboard
    │   ├── style-imoveis.css                # Estilo específico da tela de imóveis
    │   ├── style-login.css                  # Estilo específico da tela de login
    │   └── style-proprietarios.css          # Estilo específico da tela de proprietários
    ├── js/
    │   ├── mock-data.js                     # "Banco" em memória (arrays: imóveis, clientes, proprietários, corretores)
    │   ├── common.js                        # Menu ativo, aviso "em construção", menu do usuário, logout
    │   ├── login.js                         # Validação e simulação de login
    │   ├── dashboard.js                     # Estatísticas e imóveis em destaque
    │   ├── imoveis.js                       # CRUD (em memória) de imóveis
    │   ├── clientes.js                      # CRUD (em memória) de clientes
    │   ├── proprietarios.js                 # CRUD (em memória) de proprietários
    │   └── corretores.js                    # CRUD (em memória) de corretores
    └── images/
        └── Logotipo.png                     # Logotipo do projeto ZeroAndar
```

---

## Classes Java herdadas da Etapa 7

As 25 classes Java do projeto desktop (Etapas 6/7) foram copiadas para dentro deste projeto, mantendo a mesma organização em camadas, apenas com o pacote atualizado de `uc15.etapa7.zeroandar` para `uc15.etapa8.zeroandar`:

| Pacote | Classes | Papel |
|---|---|---|
| `model` | `Pessoa`, `PessoaCliente`, `PessoaCorretor`, `PessoaProprietario`, `Imovel`, `Endereco`, `TelefonePessoa` | Entidades de domínio |
| `repository` | `ClienteDAO`, `CorretorDAO`, `ImovelDAO`, `ProprietarioDAO` | Interfaces de acesso a dados |
| `repository.jdbc` | `JdbcClienteDAO`, `JdbcCorretorDAO`, `JdbcImovelDAO`, `JdbcProprietarioDAO`, `JdbcEnderecoDAO`, `JdbcUsuarioDAO` | Implementações JDBC das interfaces acima |
| `service` | `ClienteService`, `CorretorService`, `ImovelService`, `ProprietarioService`, `ValidacaoService` | Regras de negócio |
| `infrastructure` | `DatabaseConnection` | Conexão JDBC com o MySQL |
| `test` | `Main` | Classe de demonstração em console (Etapa 7) |
| `src/test/java` | `ZeroAndarTest` | Teste unitário JUnit da Etapa 7 |

**As classes de `model` / `repository` / `service` ainda não são usadas.** O enunciado da Etapa 8 pede explicitamente que ainda não haja regra de negócio nem conexão com banco de dados; elas existem no projeto apenas para dar continuidade ao trabalho já feito e servirão de base para os `@Service` / `@Repository` do Spring na Etapa 9.

O Tomcat embutido (classe `ZeroAndarApplication`, veja a seção **Como executar** abaixo) já roda de verdade nesta etapa, mas só serve os arquivos estáticos — ele ainda não chama nenhuma dessas classes de negócio. Para que essas classes compilem corretamente, o `pom.xml` já inclui `mysql-connector-j` e `junit-jupiter`, as mesmas dependências que o `pom.xml` da Etapa 7 já usava.

---

## Telas implementadas

| Tela | Arquivo | O que faz |
|---|---|---|
| Login | `login.html` | Valida e-mail/senha com JavaScript, autentica contra uma lista fixa de usuários (mock), opção "lembrar-me" (localStorage) e redireciona para o Dashboard. |
| Dashboard | `dashboard.html` | Mostra estatísticas (total de imóveis, clientes ativos, corretores, proprietários) e imóveis em destaque, tudo calculado a partir dos arrays mockados. |
| Imóveis | `imoveis.html` | Lista + formulário (master-detail) com busca, cadastro, edição e exclusão em memória. |
| Clientes | `clientes.html` | Mesma dinâmica de Imóveis, adaptada ao cadastro de clientes. |
| Proprietários | `proprietarios.html` | Mesma dinâmica, com bloqueio de exclusão se houver imóveis vinculados. |
| Corretores | `corretores.html` | Mesma dinâmica, com bloqueio de exclusão se houver imóveis vinculados. |

Os itens de menu **Agenda, Negociações, Pesquisa, Relatórios e Configurações** ainda não foram implementados: ao clicar, é exibido um aviso "funcionalidade em construção" (modal do Bootstrap), sem navegar para nenhuma página — conforme combinado, para não travar a navegação do usuário nas telas que ainda serão desenvolvidas.

---

## Bibliotecas utilizadas

- **Bootstrap 5.3.3** (via CDN) — usado no modal de aviso "em construção" e no reset básico de estilos.
- **jQuery 3.7.1** (via CDN) — usado para toda a manipulação do DOM, os formulários e o CRUD em memória.

## Validações implementadas (JavaScript)

- **Login:** formato de e-mail, senha mínima de 6 caracteres, checagem contra usuários cadastrados.
- **Clientes / Proprietários / Corretores:** nome obrigatório, CPF no formato `000.000.000-00`, e-mail em formato válido.
- **Imóveis:** descrição, proprietário e rua obrigatórios; exige ao menos um valor (venda ou aluguel).
- Campos inválidos recebem a classe `is-invalid` e uma mensagem de erro é exibida abaixo do campo.

---

## Como executar o projeto (sobe o Tomcat de verdade)

A classe que você precisa **executar (Run)** é:

```
uc15.etapa8.zeroandar.ZeroAndarApplication
```

Ela é uma aplicação Spring Boot (`@SpringBootApplication`) que sobe o Tomcat embutido e serve as páginas de `src/main/resources/static`. Não há Controllers nem regra de negócio nesta etapa — o Tomcat só entrega os arquivos HTML/CSS/JS prontos, exatamente como fariam em produção.

Pelo terminal:

```bash
mvn spring-boot:run
```

Ou, no NetBeans: clique com o botão direito em `ZeroAndarApplication.java` → **Run File**.

Depois, acesse **http://localhost:8080/login.html** no navegador (ou apenas **http://localhost:8080/**, que já redireciona para o login via o `index.html`).

### Sobre as outras classes "Main" que já existem no projeto

Não confunda com as classes herdadas das etapas anteriores — elas continuam no projeto, mas **não sobem servidor nenhum**:

| Classe | De qual etapa | O que faz |
|---|---|---|
| `uc15.etapa8.zeroandar.ZeroAndarApplication` | **Etapa 8 (nova)** | **Esta é a que você deve rodar.** Sobe o Tomcat e serve o front-end. |
| `uc15.etapa8.zeroandar.test.Main` | Herdada da Etapa 6/7 | Roda testes de validação direto no `System.out`, sem interface. Não inicia servidor. |
| `uc15.etapa8.zeroandar.test.ZeroAndarTest` (em `src/test/java`) | Herdada da Etapa 7 | Testes JUnit, executados com `mvn test`, não com "Run". |

### `static` x `templates`

- **`src/main/resources/static`** (usado agora, na Etapa 8): arquivos estáticos "prontos", servidos exatamente como estão — é onde ficam nossos HTML/CSS/JS hoje, porque ainda não há nada para processar no servidor.
- **`src/main/resources/templates`**: é para onde as páginas migram quando o Spring passa a **processá-las** antes de enviar ao navegador — no nosso caso, isso aconteceria na **Etapa 9**, se optarmos por Thymeleaf (por exemplo, para inserir dados vindos do banco direto no HTML no servidor, em vez de buscar via JavaScript). Quando chegar lá, as páginas saem de `static/` e entram em `templates/`, e passam a ser retornadas por um `@Controller` (não mais servidas diretamente pelo Tomcat). O `pom.xml` já deixa um comentário indicando exatamente onde entra a dependência do Thymeleaf quando isso acontecer.

---

## Continuidade para a Etapa 9

Os arrays de `js/mock-data.js` foram desenhados com os mesmos campos do `database/zeroandar_db.sql`, para que, na próxima etapa (back-end com Spring), baste substituir as funções de leitura/gravação desses arrays por chamadas REST ao Spring Boot, mantendo a mesma estrutura de HTML/CSS/JS.

---

## 🔑 Login de teste (repetido para referência rápida)

| Usuário | Senha |
|---|---|
| `alex@zeroandar.com` | `senha123` |
