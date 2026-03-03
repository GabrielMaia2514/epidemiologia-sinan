package com.seuprojeto.epidemiologia.controller;

import com.seuprojeto.epidemiologia.service.DoencasImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/importar")
public class DoencasImportController {

    private final DoencasImportService service;

    public DoencasImportController(DoencasImportService service) {
        this.service = service;
    }

    @PostMapping("/{doenca}/{ano}")
    public ResponseEntity<String> importar(@PathVariable int ano, @PathVariable String doenca) {
        try {
            service.importarCsvDoencaAno(ano, doenca);
            return ResponseEntity.ok("Importação para o ano " + ano + " da doença " + doenca + " sendo realizada em segundo plano!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao importar CSV para " + doenca + " no ano " + ano + ": " + e.getMessage());
        }
    }
}
