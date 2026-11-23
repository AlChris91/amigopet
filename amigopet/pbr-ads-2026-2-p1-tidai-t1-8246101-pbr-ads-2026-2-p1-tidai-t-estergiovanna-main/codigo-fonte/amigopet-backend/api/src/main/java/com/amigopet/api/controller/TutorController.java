package com.amigopet.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigopet.api.dto.AtualizacaoResponseDTO;
import com.amigopet.api.dto.LoginResponseDTO;
import com.amigopet.api.dto.TutorAtualizacaoDTO;
import com.amigopet.api.dto.TutorCadastroDTO;
import com.amigopet.api.dto.TutorLoginDTO;
import com.amigopet.api.dto.TutorResponseDTO;
import com.amigopet.api.service.ServicoTutor;

import jakarta.validation.Valid;

/**
 * Endpoints da tabela {@code tutores} (portal do tutor).
 *
 * <p>A camada de permissao de acesso ao CORS fica centralizada na classe
 * {@code CorsConfig}, evitando repetir a configuracao em cada controller.</p>
 */
@RestController
@RequestMapping("/api/tutores")
public class TutorController {

    private final ServicoTutor servicoTutor;

    public TutorController(ServicoTutor servicoTutor) {
        this.servicoTutor = servicoTutor;
    }

    /** Cadastra um novo tutor. */
    @PostMapping("/cadastro")
    public ResponseEntity<TutorResponseDTO> cadastrar(@Valid @RequestBody TutorCadastroDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoTutor.cadastrar(dados));
    }

    /** Autentica o tutor e devolve o identificador usado pelo front-end. */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody TutorLoginDTO dados) {
        return ResponseEntity.ok(servicoTutor.login(dados));
    }

    /** Atualiza o perfil do tutor. */
    @PutMapping("/{id}")
    public ResponseEntity<AtualizacaoResponseDTO> atualizar(@PathVariable Long id,
                                                           @Valid @RequestBody TutorAtualizacaoDTO dados) {
        return ResponseEntity.ok(servicoTutor.atualizar(id, dados));
    }

    /** Consulta os dados do tutor. */
    @GetMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(servicoTutor.buscarPorId(id));
    }
}
