package com.amigopet.api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinica_id", nullable = false)
    private Long clinicaId;

    @Column(name = "nome_servico", nullable = false, length = 50)
    private String nomeServico;

    private BigDecimal preco;
    private String especieAtendida;
    private String orientacaoPreAtendimento;    

    public Servico() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClinicaId() { return clinicaId; }
    public void setClinicaId(Long clinicaId) { this.clinicaId = clinicaId; }
    public String getNomeServico() { return nomeServico; }
    public void setNomeServico(String nomeServico) { this.nomeServico = nomeServico; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public String getEspecieAtendida() { return especieAtendida; }
    public void setEspecieAtendida(String especieAtendida) { this.especieAtendida = especieAtendida; }

    public String getOrientacaoPreAtendimento() { return orientacaoPreAtendimento; }
    public void setOrientacaoPreAtendimento(String orientacaoPreAtendimento) { this.orientacaoPreAtendimento = orientacaoPreAtendimento; }
}
