package br.com.leonbooks.leon_books.controller;

import br.com.leonbooks.leon_books.model.Multa;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/multas")
public class MultaController {
    private final EmprestimoService emprestimoService;

    public MultaController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Multa>> listarMultasCliente(@PathVariable Long clienteId) {
        List<Multa> multas = emprestimoService.buscarMultasPorCliente(clienteId);
        return ResponseEntity.ok(multas);
    }

    @GetMapping("/cliente/{clienteId}/total")
    public ResponseEntity<BigDecimal> calcularTotalMultasCliente(@PathVariable Long clienteId) {
        BigDecimal total = emprestimoService.calcularTotalMultasCliente(clienteId);
        return ResponseEntity.ok(total);
    }

    @PostMapping("/{multaId}/pagar")
    public ResponseEntity<Void> pagarMulta(@PathVariable Long multaId) {
        emprestimoService.pagarMulta(multaId);
        return ResponseEntity.noContent().build();
    }
}