package uc15.etapa9.zeroandar.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import uc15.etapa9.zeroandar.model.PessoaCorretor;
import uc15.etapa9.zeroandar.service.CorretorService;
import uc15.etapa9.zeroandar.web.WebDataMapper;

/** Controller Spring MVC responsável pelo CRUD de corretores. */
@Controller
@ResponseBody
@RequestMapping("/corretores")
public class CorretorController {
    private final CorretorService service = new CorretorService();

    @GetMapping
    public List<PessoaCorretor> listar(@RequestParam(required = false) String busca) {
        return busca == null || busca.isBlank() ? service.listarTodos() : service.buscarPorTexto(busca);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaCorretor> buscar(@PathVariable int id) {
        PessoaCorretor p = service.buscarPorId(id);
        return p == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<?> inserir(@RequestBody Map<String, Object> dados) {
        try {
            PessoaCorretor p = WebDataMapper.corretor(dados);
            if (p.getEndereco() != null) {
                // A tela da Etapa 8 não possui campo para bairro e CEP.
                // O banco exige esses campos, então usamos vazio no cadastro.
                if (p.getEndereco().getBairro() == null) p.getEndereco().setBairro("");
                if (p.getEndereco().getCep() == null) p.getEndereco().setCep("");
            }
            if (service.adicionar(p)) return ResponseEntity.ok(service.buscarPorId(p.getIdPessoaCorretor()));
            return ResponseEntity.badRequest().body("Não foi possível cadastrar o corretor.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable int id, @RequestBody Map<String, Object> dados) {
        try {
            PessoaCorretor atual = service.buscarPorId(id);
            if (atual == null) return ResponseEntity.notFound().build();

            PessoaCorretor p = WebDataMapper.corretor(dados);
            p.setIdPessoaCorretor(id);
            p.setIdPessoa(atual.getIdPessoa());
            if (p.getDataAdmissao() == null) p.setDataAdmissao(atual.getDataAdmissao());

            if (p.getEndereco() != null && atual.getEndereco() != null) {
                p.getEndereco().setIdEndereco(atual.getEndereco().getIdEndereco());
                if (p.getEndereco().getCep() == null || p.getEndereco().getCep().isBlank()) {
                    p.getEndereco().setCep(atual.getEndereco().getCep());
                }
                if (p.getEndereco().getBairro() == null || p.getEndereco().getBairro().isBlank()) {
                    p.getEndereco().setBairro(atual.getEndereco().getBairro());
                }
                if (p.getEndereco().getComplemento() == null || p.getEndereco().getComplemento().isBlank()) {
                    p.getEndereco().setComplemento(atual.getEndereco().getComplemento());
                }
            }

            if (service.atualizar(p)) return ResponseEntity.ok(service.buscarPorId(id));
            return ResponseEntity.badRequest().body("Não foi possível atualizar o corretor.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {
        PessoaCorretor p = service.buscarPorId(id);
        return p != null && service.excluir(p.getCreci()) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
