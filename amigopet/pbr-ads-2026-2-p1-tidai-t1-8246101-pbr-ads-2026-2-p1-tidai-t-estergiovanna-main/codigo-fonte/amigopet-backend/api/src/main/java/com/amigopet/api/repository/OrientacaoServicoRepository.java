package com.amigopet.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amigopet.api.model.OrientacaoServico;

public interface OrientacaoServicoRepository extends JpaRepository<OrientacaoServico, Long> {

    /** Lista as orientacoes de um servico, ordenadas pelo id para manter a exibicao estavel. */
    List<OrientacaoServico> findByServicoIdOrderByIdAsc(Long servicoId);

    /** Lista as orientacoes de varios servicos de uma vez, evitando consulta por clinica. */
    List<OrientacaoServico> findByServicoIdIn(List<Long> servicoIds);
}
