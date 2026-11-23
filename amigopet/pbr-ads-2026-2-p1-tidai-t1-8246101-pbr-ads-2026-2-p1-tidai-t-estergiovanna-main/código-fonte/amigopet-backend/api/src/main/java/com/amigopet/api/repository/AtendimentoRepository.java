package com.amigopet.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amigopet.api.model.Atendimento;
import com.amigopet.api.model.StatusAtendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    /** Atendimentos de uma clinica que permanecem em um determinado status da fila. */
    List<Atendimento> findByClinicaIdAndStatusAtendimento(Long clinicaId, StatusAtendimento status);

    /** Fila da clinica ordenada pela ordem de chegada, que define a posicao exibida ao tutor. */
    List<Atendimento> findByClinicaIdAndStatusAtendimentoOrderByDataRegistroAscIdAsc(Long clinicaId, StatusAtendimento status);

    /** Historico de atendimentos de um tutor, utilizado na tela de acompanhamento. */
    List<Atendimento> findByTutorIdOrderByDataRegistroDesc(Long tutorId);
}
