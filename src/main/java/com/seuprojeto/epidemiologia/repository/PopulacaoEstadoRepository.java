package com.seuprojeto.epidemiologia.repository;

import com.seuprojeto.epidemiologia.entity.PopulacaoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PopulacaoEstadoRepository extends JpaRepository<PopulacaoEstado, Integer> {
    Optional<PopulacaoEstado> findByEstadoIdAndAno(Integer estadoId, int ano);
}