package com.project.byeoryback.domain.persona.repository;

import com.project.byeoryback.domain.persona.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByUserId(Long userId);
}
