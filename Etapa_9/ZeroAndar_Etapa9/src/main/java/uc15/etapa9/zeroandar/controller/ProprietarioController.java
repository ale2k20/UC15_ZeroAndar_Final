package uc15.etapa9.zeroandar.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import uc15.etapa9.zeroandar.model.PessoaProprietario;
import uc15.etapa9.zeroandar.service.ProprietarioService;
import uc15.etapa9.zeroandar.web.WebDataMapper;

/** Controller Spring MVC responsável pelo CRUD de proprietários. */
@Controller
@ResponseBody
@RequestMapping("/proprietarios")
public class ProprietarioController {
    private final ProprietarioService service = new ProprietarioService();

    @GetMapping
    public List<PessoaProprietario> listar(@RequestParam(required = false) String busca) {
        return busca == null || busca.isBlank() ? service.listarTodos() : service.buscarPorTexto(busca);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaProprietario> buscar(@PathVariable int id) {
        PessoaProprietario p = service.buscarPorId(id);
        return p == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<?> inserir(@RequestBody Map<String, Object> dados) {
        try {
            PessoaProprietario p = WebDataMapper.proprietario(dados);
            if (p.getEndereco() != null) {
                // A tela da Etapa 8 não possui campo para bairro e CEP.
                // O banco exige esses campos, então usamos vazio no cadastro.
                if (p.getEndereco().getBairro() == null) p.getEndereco().setBairro("");
                if (p.getEndereco().getCep() == null) p.getEndereco().setCep("");
            }
            if (service.adicionar(p)) return ResponseEntity.ok(service.buscarPorId(p.getIdPessoaProprietario()));
            return ResponseEntity.badRequest().body("Não foi possível cadastrar o proprietário.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable int id, @RequestBody Map<String, Object> dados) {
        try {
            PessoaProprietario atual = service.buscarPorId(id);
            if (atual == null) return ResponseEntity.notFound().build();

            PessoaProprietario p = WebDataMapper.proprietario(dados);
            p.setIdPessoaProprietario(id);
            p.setIdPessoa(atual.getIdPessoa());
            if (p.getDataCadastro() == null) p.setDataCadastro(atual.getDataCadastro());

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
            return ResponseEntity.badRequest().body("Não foi possível atualizar o proprietário.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {
        return service.excluir(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
