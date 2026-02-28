package com.seuprojeto.epidemiologia.repository;

import com.seuprojeto.epidemiologia.entity.Doenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoencaRepository extends JpaRepository<Doenca, Integer> {

    Optional<Doenca> findByNome(String nome);

}
