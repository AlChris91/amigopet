package com.amigopet.api.repository;

import com.amigopet.api.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByEmail(String email);
}
