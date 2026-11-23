package com.amigopet.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "atendimentos")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tutor_id", nullable = false)
    private Long tutorId;

    @Column(name = "clinica_id", nullable = false)
    private Long clinicaId;

    @Column(name = "servico_id", nullable = false)
    private Long servicoId;

    @Column(name = "especie_animal", nullable = false, length = 30)
    private String especieAnimal;

    @Column(name = "porte_animal", nullable = false, length = 30)
    private String porteAnimal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_atendimento", length = 30)
    private StatusAtendimento statusAtendimento = StatusAtendimento.BUSCANDO;

    @Column(name = "posicao_fila")
    private Integer posicaoFila = 0;

    @Column(name = "tempo_estimado_minutos")
    private Integer tempoEstimadoMinutos = 0;

    @Column(name = "data_registro", updatable = false)
    private LocalDateTime dataRegistro;

    @PrePersist
    protected void onCreate() {
        this.dataRegistro = LocalDateTime.now();
    }

    public Atendimento() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }
    public Long getClinicaId() { return clinicaId; }
    public void setClinicaId(Long clinicaId) { this.clinicaId = clinicaId; }
    public Long getServicoId() { return servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public String getEspecieAnimal() { return especieAnimal; }
    public void setEspecieAnimal(String especieAnimal) { this.especieAnimal = especieAnimal; }
    public String getPorteAnimal() { return porteAnimal; }
    public void setPorteAnimal(String porteAnimal) { this.porteAnimal = porteAnimal; }
    public String getStatusAtendimento() {
        return statusAtendimento == null ? null : statusAtendimento.name();
    }

    public void setStatusAtendimento(String statusAtendimento) {
        this.statusAtendimento = StatusAtendimento.isValido(statusAtendimento)
                ? StatusAtendimento.valueOf(statusAtendimento.trim().toUpperCase())
                : null;
    }

    public StatusAtendimento getStatus() {
        return statusAtendimento;
    }

    public void setStatus(StatusAtendimento statusAtendimento) {
        this.statusAtendimento = statusAtendimento;
    }
    public Integer getPosicaoFila() { return posicaoFila; }
    public void setPosicaoFila(Integer posicaoFila) { this.posicaoFila = posicaoFila; }
    public Integer getTempoEstimadoMinutos() { return tempoEstimadoMinutos; }
    public void setTempoEstimadoMinutos(Integer tempoEstimadoMinutos) { this.tempoEstimadoMinutos = tempoEstimadoMinutos; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
}
