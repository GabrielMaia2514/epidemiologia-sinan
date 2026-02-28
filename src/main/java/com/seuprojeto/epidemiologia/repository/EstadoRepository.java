package com.seuprojeto.epidemiologia.repository;

import com.seuprojeto.epidemiologia.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    Optional<Estado> findBySigla(String uf);
}
