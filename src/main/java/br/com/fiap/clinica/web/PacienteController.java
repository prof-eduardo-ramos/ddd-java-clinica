package br.com.fiap.clinica.web;

import br.com.fiap.clinica.application.PacienteService;
import br.com.fiap.clinica.web.dto.CadastrarPacienteRequest;
import br.com.fiap.clinica.web.dto.PacienteResponse;
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

/** Controller fino: recebe HTTP, chama o caso de uso e devolve o DTO. Nenhuma regra aqui. */
@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> cadastrar(@RequestBody @Valid CadastrarPacienteRequest request) {
        var paciente = service.cadastrar(request.nome(), request.cpf(), request.email());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(PacienteResponse.de(paciente));
    }

    @GetMapping
    public List<PacienteResponse> listar() {
        return service.listar().stream().map(PacienteResponse::de).toList();
    }

    @GetMapping("/{id}")
    public PacienteResponse buscar(@PathVariable UUID id) {
        return PacienteResponse.de(service.buscar(id));
    }
}
