# ZERO ANDAR - PI3 Etapa 9

## Objetivo

Integração do front-end desenvolvido na Etapa 8 com o back-end Java/Spring MVC e o banco de dados MySQL.

## Tecnologias

- Java 21+
- Spring Boot
- Spring MVC (`@Controller` + `@ResponseBody`)
- JDBC
- MySQL
- Maven
- HTML, CSS, JavaScript e jQuery

## Banco de dados

A conexão JDBC usa:

`jdbc:mysql://localhost:3306/zandarDB?useSSL=false&serverTimezone=America/Sao_Paulo`

## Estrutura do back-end

```text
Controller -> Service -> DAO JDBC -> MySQL
```

Não foi criado um projeto REST separado. Os Controllers são Spring MVC e usam `@ResponseBody` para devolver os dados consumidos pelo JavaScript da própria aplicação.

## CRUD integrado

Foram integrados os quatro cadastros principais:

- Imóveis
- Clientes
- Proprietários
- Corretores

Também foram integrados:

- login com sessão HTTP;
- dashboard com contagens do banco;
- carregamento de proprietários e corretores no cadastro de imóvel;
- persistência do telefone principal em `TelefonePessoa`;
- endereços pela estrutura JDBC já existente.

## Execução

1. Crie o banco executando `database/zeroandar_db.sql` no MySQL.
2. Confirme que o MySQL está disponível na porta 3306.
3. Abra o projeto como projeto Maven no NetBeans ou outra IDE.
4. Execute:

```text
mvn spring-boot:run
```

5. Acesse:

```text
http://localhost:8080/
```

## Login do script do banco

Use um dos usuários inseridos pelo script, por exemplo:

- `admin@zandar.com` / `senha123`
- `ricardo@zandar.com` / `senha123`
- `patricia@zandar.com` / `senha123`

## Observação sobre testes

Os testes unitários existentes da Etapa 8 foram preservados. As classes Java de modelo, repository, service, controller e web foram verificadas quanto à sintaxe neste ambiente e os arquivos JavaScript foram verificados com o Node.js.

A execução completa do Maven e a conexão real com MySQL precisam ser realizadas em um ambiente que tenha Maven e MySQL instalados e em execução.
