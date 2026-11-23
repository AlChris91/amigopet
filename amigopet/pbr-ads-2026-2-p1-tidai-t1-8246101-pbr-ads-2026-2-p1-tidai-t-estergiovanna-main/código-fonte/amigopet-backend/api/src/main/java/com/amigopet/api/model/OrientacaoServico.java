package com.amigopet.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Orientacao pre-atendimento vinculada a um servico.
 *
 * <p>Corresponde a tabela {@code orientacoes_servico} criada no script de
 * banco de dados e exibida na tela de resumo do agendamento.</p>
 */
@Entity
@Table(name = "orientacoes_servico")
public class OrientacaoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "servico_id", nullable = false)
    private Long servicoId;

    @Column(name = "descricao_orientacao", nullable = false, length = 255)
    private String descricaoOrientacao;

    public OrientacaoServico() {
    }

    public OrientacaoServico(Long servicoId, String descricaoOrientacao) {
        this.servicoId = servicoId;
        this.descricaoOrientacao = descricaoOrientacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getDescricaoOrientacao() {
        return descricaoOrientacao;
    }

    public void setDescricaoOrientacao(String descricaoOrientacao) {
        this.descricaoOrientacao = descricaoOrientacao;
    }
}
