package com.amigopet.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amigopet.api.dto.AtualizacaoResponseDTO;
import com.amigopet.api.dto.ClinicaAtualizacaoDTO;
import com.amigopet.api.dto.ClinicaBuscaResponseDTO;
import com.amigopet.api.dto.ClinicaCadastroDTO;
import com.amigopet.api.dto.ClinicaLoginDTO;
import com.amigopet.api.dto.ClinicaResponseDTO;
import com.amigopet.api.dto.LoginResponseDTO;
import com.amigopet.api.dto.ServicoCadastroDTO;
import com.amigopet.api.dto.ServicoResponseDTO;
import com.amigopet.api.model.Clinica;
import com.amigopet.api.repository.ClinicaRepository;
import com.amigopet.api.service.ServicoClinica;
import com.amigopet.api.service.ServicoServico;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clinicas")
public class ClinicaController {

    private final ServicoClinica servicoClinica;
    private final ServicoServico servicoServico;
    private final ClinicaRepository clinicaRepository;

    public ClinicaController(ServicoClinica servicoClinica, ServicoServico servicoServico, ClinicaRepository clinicaRepository) {
        this.servicoClinica = servicoClinica;
        this.servicoServico = servicoServico;
        this.clinicaRepository = clinicaRepository;
    }

    @GetMapping("/busca")
    public ResponseEntity<List<ClinicaBuscaResponseDTO>> buscar(
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) String servico,
            @RequestParam(required = false) String especie, // ADICIONADO AQUI
            @RequestParam(required = false) Double raio,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        // Repassa a espécie para a classe de serviço filtrar
        return ResponseEntity.ok(servicoClinica.buscar(cep, servico, especie, raio, latitude, longitude));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<ClinicaResponseDTO> cadastrar(@Valid @RequestBody ClinicaCadastroDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoClinica.cadastrar(dados));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody ClinicaLoginDTO dados) {
        return ResponseEntity.ok(servicoClinica.login(dados));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtualizacaoResponseDTO> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody ClinicaAtualizacaoDTO dados) {
        return ResponseEntity.ok(servicoClinica.atualizar(id, dados));
    }

    @GetMapping("/{id}/servicos")
    public ResponseEntity<List<ServicoResponseDTO>> listarServicos(@PathVariable Long id) {
        return ResponseEntity.ok(servicoServico.listarPorClinica(id));
    }

    @PostMapping("/{id}/servicos")
    public ResponseEntity<ServicoResponseDTO> adicionarServico(
            @PathVariable Long id, 
            @Valid @RequestBody ServicoCadastroDTO dados) {
        
        ServicoCadastroDTO dadosComClinica = new ServicoCadastroDTO(
            id, 
            dados.nomeServico(), 
            dados.preco(), 
            dados.especieAtendida(), 
            dados.orientacaoPreAtendimento()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoServico.cadastrar(dadosComClinica));
    }

    @GetMapping("/servicos")
    public ResponseEntity<List<ServicoResponseDTO>> listarTodosOsServicos() {
        return ResponseEntity.ok(servicoServico.listarTodos());
    }

    @GetMapping("/todas")
    public ResponseEntity<List<Clinica>> listarTodasAsClinicas() {
        return ResponseEntity.ok(clinicaRepository.findAll());
    }
}
