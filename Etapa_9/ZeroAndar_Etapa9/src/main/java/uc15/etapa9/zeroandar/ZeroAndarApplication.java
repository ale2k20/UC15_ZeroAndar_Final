/**
 * UC15 - Projeto Integrador 3 - Etapa 9
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.9
 */
package uc15.etapa9.zeroandar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação web. É esta classe que deve ser executada
 * (Run) para subir o servidor Tomcat embutido do Spring Boot e servir as
 * páginas do front-end integrado ao back-end (HTML/CSS/JS) que estão em
 * {@code src/main/resources/static}.
 *
 * <p>Diferente da {@link uc15.etapa9.zeroandar.test.Main}, que é apenas
 * uma classe de demonstração em console herdada da Etapa 7, esta classe
 * ({@code ZeroAndarApplication}) é a que efetivamente inicia o servidor
 * web.</p>
 *
 * <p>Nesta etapa, os {@code @Controller} Spring MVC atendem as requisições do
 * front-end e reaproveitam as classes de
 * {@code model}, {@code repository} e {@code service} já presentes no
 * projeto, com acesso JDBC ao MySQL.</p>
 *
 * <p>Como executar:</p>
 * <pre>
 *   mvn spring-boot:run
 * </pre>
 * <p>ou, pelo NetBeans, executando (Run File) esta classe diretamente.
 * Depois, acesse http://localhost:8080/login.html no navegador.</p>
 */
@SpringBootApplication
public class ZeroAndarApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroAndarApplication.class, args);
    }
}
