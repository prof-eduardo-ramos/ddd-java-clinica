package br.com.fiap.clinica.presentation;

import br.com.fiap.clinica.application.service.ConsultaService;
import br.com.fiap.clinica.presentation.dto.AgendarConsultaRequest;
import br.com.fiap.clinica.presentation.dto.CancelarConsultaRequest;
import br.com.fiap.clinica.presentation.dto.ConsultaResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConsultaResponse> agendar(@RequestBody @Valid AgendarConsultaRequest request) {
        var consulta = service.agendar(
                request.paciente().paraPaciente(),
                request.medico().paraMedico(),
                request.dataHora());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(consulta.getId()).toUri();
        return ResponseEntity.created(uri).body(ConsultaResponse.de(consulta));
    }

    @GetMapping
    public List<ConsultaResponse> listar() {
        return service.listar().stream().map(ConsultaResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ConsultaResponse buscar(@PathVariable UUID id) {
        return ConsultaResponse.de(service.buscar(id));
    }

    @PostMapping("/{id}/cancelamento")
    public ConsultaResponse cancelar(@PathVariable UUID id, @RequestBody @Valid CancelarConsultaRequest request) {
        return ConsultaResponse.de(service.cancelar(id, request.motivo()));
    }

    @PostMapping("/{id}/conclusao")
    public ConsultaResponse concluir(@PathVariable UUID id) {
        return ConsultaResponse.de(service.concluir(id));
    }
}
