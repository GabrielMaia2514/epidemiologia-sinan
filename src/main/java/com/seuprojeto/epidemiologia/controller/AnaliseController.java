package com.seuprojeto.epidemiologia.controller;
import com.seuprojeto.epidemiologia.dto.AnaliseEstadoResponse;
import com.seuprojeto.epidemiologia.service.AnaliseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analise")
public class AnaliseController {
    private final AnaliseService analiseService;

    public AnaliseController(AnaliseService service) {
        this.analiseService = service;
    }

    @GetMapping("/estado-anual/{uf}/{doenca}")
    public ResponseEntity<?> analisarEstado(@PathVariable("uf") String uf, @PathVariable("doenca") String doenca) {
        try {
            AnaliseEstadoResponse response = analiseService.analisarEstadoPorAno(uf, doenca);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
