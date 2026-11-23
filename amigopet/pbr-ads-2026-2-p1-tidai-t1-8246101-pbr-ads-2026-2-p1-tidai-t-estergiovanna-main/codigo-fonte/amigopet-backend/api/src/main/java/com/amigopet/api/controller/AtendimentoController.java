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
import org.springframework.web.bind.annotation.RestController;

import com.amigopet.api.dto.AtendimentoRequestDTO;
import com.amigopet.api.dto.AtendimentoResponseDTO;
import com.amigopet.api.dto.AtendimentoStatusDTO;
import com.amigopet.api.service.ServicoAtendimento;

import jakarta.validation.Valid;

/**
 * Endpoints da fila virtual de atendimentos ({@code atendimentos}).
 */
@RestController
@RequestMapping("/api/atendimentos")
public class AtendimentoController {

    private final ServicoAtendimento servicoAtendimento;

    public AtendimentoController(ServicoAtendimento servicoAtendimento) {
        this.servicoAtendimento = servicoAtendimento;
    }

    /** Registra a entrada do tutor na fila da clinica. */
    @PostMapping
    public ResponseEntity<AtendimentoResponseDTO> registrar(@Valid @RequestBody AtendimentoRequestDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoAtendimento.registrar(dados));
    }

    /** Lista a fila de atendimento de uma clinica. */
    @GetMapping("/clinica/{clinicaId}")
    public ResponseEntity<List<AtendimentoResponseDTO>> listarFila(@PathVariable Long clinicaId) {
        return ResponseEntity.ok(servicoAtendimento.listarFila(clinicaId));
    }

    /** Consulta a situacao de um atendimento, usada pela tela de acompanhamento. */
    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(servicoAtendimento.buscarPorId(id));
    }

    /** Altera o status de um atendimento e reorganiza a fila da clinica. */
    @PutMapping("/{id}/status")
    public ResponseEntity<AtendimentoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                                 @Valid @RequestBody AtendimentoStatusDTO dados) {
        return ResponseEntity.ok(servicoAtendimento.atualizarStatus(id, dados.status()));
    }
}
