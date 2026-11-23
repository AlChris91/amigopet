package com.amigopet.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amigopet.api.model.Clinica;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
    Optional<Clinica> findByEmailAdm(String emailAdm);
}
