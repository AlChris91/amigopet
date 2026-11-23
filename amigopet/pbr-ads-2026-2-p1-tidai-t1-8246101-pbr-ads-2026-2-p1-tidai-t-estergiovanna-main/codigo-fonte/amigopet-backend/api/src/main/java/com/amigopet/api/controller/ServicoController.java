package com.amigopet.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigopet.api.dto.OrientacaoCadastroDTO;
import com.amigopet.api.dto.OrientacaoResponseDTO;
import com.amigopet.api.dto.ServicoCadastroDTO;
import com.amigopet.api.dto.ServicoResponseDTO;
import com.amigopet.api.service.ServicoServico;

import jakarta.validation.Valid;

/**
 * Endpoints dos servicos oferecidos pelas clinicas e das orientacoes
 * pre-atendimento exibidas na confirmacao do agendamento.
 */
@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoServico servicoServico;

    public ServicoController(ServicoServico servicoServico) {
        this.servicoServico = servicoServico;
    }

    /** Lista todos os servicos cadastrados no sistema. */
    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(servicoServico.listarTodos());
    }

    /** Cadastra um servico para uma clinica parceira. */
    @PostMapping
    public ResponseEntity<ServicoResponseDTO> cadastrar(@Valid @RequestBody ServicoCadastroDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoServico.cadastrar(dados));
    }

    /** Remove um servico e as orientacoes vinculadas a ele. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        servicoServico.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /** Cadastra uma orientacao pre-atendimento para um servico. */
    @PostMapping("/orientacoes")
    public ResponseEntity<OrientacaoResponseDTO> cadastrarOrientacao(
            @Valid @RequestBody OrientacaoCadastroDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoServico.cadastrarOrientacao(dados));
    }

    /** Lista as orientacoes pre-atendimento de um servico. */
    @GetMapping("/{id}/orientacoes")
    public ResponseEntity<List<OrientacaoResponseDTO>> listarOrientacoes(@PathVariable Long id) {
        return ResponseEntity.ok(servicoServico.listarOrientacoes(id));
    }

    /**
     * Lista as orientacoes de todos os servicos de uma clinica, agrupadas por servico.
     *
     * @param id identificador da clinica
     * @return orientacoes agrupadas pelo identificador do servico
     */
    @GetMapping("/clinica/{id}/orientacoes")
    public ResponseEntity<Map<Long, List<OrientacaoResponseDTO>>> listarOrientacoesPorClinica(@PathVariable Long id) {
        return ResponseEntity.ok(servicoServico.listarOrientacoesPorClinica(id));
    }
}
