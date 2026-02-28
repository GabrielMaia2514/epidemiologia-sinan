package com.seuprojeto.epidemiologia.repository;

import com.seuprojeto.epidemiologia.entity.CategoriaDoenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaDoencaRepository extends JpaRepository<CategoriaDoenca, Integer> {
    Optional<CategoriaDoenca> findByNome(String nome);
}
