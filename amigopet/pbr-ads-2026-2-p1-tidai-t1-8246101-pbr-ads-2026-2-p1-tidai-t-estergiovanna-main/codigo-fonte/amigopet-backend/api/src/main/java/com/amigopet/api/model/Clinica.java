package com.amigopet.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clinicas")
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_fantasia", nullable = false, length = 100)
    private String nomeFantasia;

    @Column(name = "email_adm", nullable = false, unique = true, length = 100)
    private String emailAdm;

    @Column(name = "senha_adm", nullable = false)
    private String senhaAdm;

    @Column(length = 10)
    private String cep;

    private String endereco;
    private String telefone;
    private Double latitude;
    private Double longitude;
    private String especieAtendida;

    public Clinica() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public String getEmailAdm() { return emailAdm; }
    public void setEmailAdm(String emailAdm) { this.emailAdm = emailAdm; }
    public String getSenhaAdm() { return senhaAdm; }
    public void setSenhaAdm(String senhaAdm) { this.senhaAdm = senhaAdm; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude ) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getEspecieAtendida() {
    return especieAtendida;
}

public void setEspecieAtendida(String especieAtendida) {
    this.especieAtendida = especieAtendida;
}
}
