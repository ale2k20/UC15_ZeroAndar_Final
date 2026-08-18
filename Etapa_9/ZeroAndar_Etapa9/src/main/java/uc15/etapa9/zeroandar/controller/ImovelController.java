package uc15.etapa9.zeroandar.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import uc15.etapa9.zeroandar.model.Imovel;
import uc15.etapa9.zeroandar.repository.jdbc.JdbcImovelDAO;
import uc15.etapa9.zeroandar.service.ImovelService;
import uc15.etapa9.zeroandar.web.WebDataMapper;

/** Controller Spring MVC responsável pelo CRUD de imóveis. */
@Controller
@ResponseBody
@RequestMapping("/imoveis")
public class ImovelController {
    private final ImovelService service = new ImovelService();
    private final JdbcImovelDAO dao = new JdbcImovelDAO();

    @GetMapping
    public List<Imovel> listar(@RequestParam(required = false) String busca) {
        if (busca == null || busca.isBlank()) {
            return service.listarTodos();
        }
        return service.buscarPorTexto(busca);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imovel> buscar(@PathVariable int id) {
        try {
            Imovel i = dao.buscarPorId(id);
            return i == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(i);
        } catch (Exception e) { return ResponseEntity.internalServerError().build(); }
    }

    @GetMapping("/destaques")
    public List<Imovel> destaques() { return service.listarDestaques(); }

    @PostMapping
    public ResponseEntity<?> inserir(@RequestBody Map<String,Object> dados) {
        try {
            Imovel i = WebDataMapper.imovel(dados);
            if (i.getCodigoImovel() == null || i.getCodigoImovel().isBlank()) i.setCodigoImovel(proximoCodigo());
            if (service.adicionar(i)) return ResponseEntity.ok(i);
            return ResponseEntity.badRequest().body("Não foi possível cadastrar o imóvel.");
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable int id, @RequestBody Map<String,Object> dados) {
        try {
            Imovel atual = dao.buscarPorId(id);
            if (atual == null) return ResponseEntity.notFound().build();
            Imovel i = WebDataMapper.imovel(dados);
            i.setIdImovel(id);
            i.setCodigoImovel(atual.getCodigoImovel());
            // Mantém o endereço existente quando o formulário não informa um ID.
            if (i.getEndereco() != null && atual.getEndereco() != null) {
                i.getEndereco().setIdEndereco(atual.getEndereco().getIdEndereco());
                // Como o formulário não possui CEP, preservamos o CEP que já existe.
                if (i.getEndereco().getCep() == null || i.getEndereco().getCep().isBlank()) {
                    i.getEndereco().setCep(atual.getEndereco().getCep());
                }
            }
            return service.atualizar(i) ? ResponseEntity.ok(i) : ResponseEntity.badRequest().body("Não foi possível atualizar o imóvel.");
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {
        try {
            Imovel i = dao.buscarPorId(id);
            return i != null && service.excluir(i.getCodigoImovel()) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    private String proximoCodigo() throws Exception {
        // Usa o próximo ID lógico do banco. Em caso de exclusão, procura um código livre.
        int n = dao.contarTotal() + 1;
        String codigo;
        do { codigo = String.format("IMOV-%03d", n++); } while (dao.buscarPorCodigo(codigo) != null);
        return codigo;
    }
}
