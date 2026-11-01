package br.com.fiap.clinica.web;

import br.com.fiap.clinica.application.MedicoService;
import br.com.fiap.clinica.web.dto.CadastrarMedicoRequest;
import br.com.fiap.clinica.web.dto.MedicoResponse;
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
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MedicoResponse> cadastrar(@RequestBody @Valid CadastrarMedicoRequest request) {
        var medico = service.cadastrar(request.nome(), request.crm(), request.uf(), request.especialidade());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(MedicoResponse.de(medico));
    }

    @GetMapping
    public List<MedicoResponse> listar() {
        return service.listar().stream().map(MedicoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public MedicoResponse buscar(@PathVariable UUID id) {
        return MedicoResponse.de(service.buscar(id));
    }
}
