package uc15.etapa9.zeroandar.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import uc15.etapa9.zeroandar.infrastructure.DatabaseConnection;
import uc15.etapa9.zeroandar.service.*;

/**
 * Fornece ao Dashboard somente dados que existem no banco de dados.
 */
@Controller
@ResponseBody
@RequestMapping("/dashboard")
public class DashboardController {
    private final ImovelService imoveis = new ImovelService();
    private final ClienteService clientes = new ClienteService();
    private final CorretorService corretores = new CorretorService();

    @GetMapping
    public Map<String,Object> dados() {
        return Map.of(
            "totalImoveis", imoveis.contarTotal(),
            "clientesAtivos", clientes.contarAtivos(),
            "corretores", corretores.contarOnline(),
            "negociacoesAtivas", contarNegociacoesAtivas(),
            "imoveisDestaque", imoveis.listarDestaques()
        );
    }

    /** Conta as negociações que ainda estão em andamento. */
    private int contarNegociacoesAtivas() {
        String sql = "SELECT COUNT(*) FROM Negociacao "
                + "WHERE status IN ('em_negociacao', 'proposta_enviada', 'proposta_aceita', 'contrato_assinado')";
        try (Connection c = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
