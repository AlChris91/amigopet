package com.amigopet.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amigopet.api.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    /** Servicos oferecidos por uma clinica, usados no filtro da busca e na validacao do atendimento. */
    List<Servico> findByClinicaIdOrderByNomeServicoAsc(Long clinicaId);

    /** Servicos de um conjunto de clinicas, busca em lote durante o filtro de distancia. */
    List<Servico> findByClinicaIdIn(List<Long> clinicaIds);
}
