package uc15.etapa9.zeroandar.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;
import uc15.etapa9.zeroandar.model.Pessoa;
import uc15.etapa9.zeroandar.repository.jdbc.JdbcUsuarioDAO;

/** Controller simples de autenticação usando HttpSession. */
@Controller
@ResponseBody
@RequestMapping("/login")
public class LoginController {
    private final JdbcUsuarioDAO usuarioDAO = new JdbcUsuarioDAO();

    @PostMapping
    public ResponseEntity<?> entrar(@RequestBody Map<String,String> dados, HttpSession session) {
        try {
            Pessoa pessoa = usuarioDAO.autenticar(dados.get("email"), dados.get("senha"));
            if (pessoa == null) return ResponseEntity.status(401).body("E-mail ou senha inválidos.");
            session.setAttribute("usuario", pessoa);
            return ResponseEntity.ok(Map.of("nome", pessoa.getNome(), "email", pessoa.getEmail(), "id", pessoa.getIdPessoa()));
        } catch (Exception e) { return ResponseEntity.internalServerError().body("Erro ao acessar o banco de dados."); }
    }

    @GetMapping("/sessao")
    public ResponseEntity<?> sessao(HttpSession session) {
        Pessoa p = (Pessoa) session.getAttribute("usuario");
        return p == null ? ResponseEntity.status(401).build() : ResponseEntity.ok(Map.of("nome", p.getNome(), "email", p.getEmail(), "id", p.getIdPessoa()));
    }

    @PostMapping("/sair")
    public ResponseEntity<Void> sair(HttpSession session) { session.invalidate(); return ResponseEntity.noContent().build(); }
}
