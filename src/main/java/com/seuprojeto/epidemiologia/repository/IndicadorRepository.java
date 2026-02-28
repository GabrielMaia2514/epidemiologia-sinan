package com.seuprojeto.epidemiologia.repository;

import com.seuprojeto.epidemiologia.entity.Indicador;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IndicadorRepository extends JpaRepository<Indicador, Integer> {

    @Modifying
    @Transactional
    @Query(
            value = """
            INSERT INTO indicadores (estado_id, doenca_id, ano, mes, casos, obitos, taxa_por100k)
            VALUES (:estadoId, :doencaId, :ano, :mes, :casos, :obitos, :taxa)
            ON CONFLICT (estado_id, doenca_id, ano, mes)
            DO UPDATE SET casos = EXCLUDED.casos, 
                          obitos = EXCLUDED.obitos, 
                          taxa_por100k = EXCLUDED.taxa_por100k
        """,
            nativeQuery = true
    )
    void upsertIndicador(@Param("estadoId") Long estadoId,
                         @Param("doencaId") Long doencaId,
                         @Param("ano") int ano,
                         @Param("mes") int mes,
                         @Param("casos") int casos,
                         @Param("obitos") int obitos,
                         @Param("taxa") BigDecimal taxa);

    List<Indicador> findByEstadoIdAndAno(Integer estadoId, Integer ano);

    List<Indicador> findByDoencaIdAndAno(Integer doencaId, Integer ano);

    List<Indicador> findByEstadoIdAndDoencaIdAndAnoAndMes(
            Integer estadoId,
            Integer doencaId,
            Integer ano,
            Integer mes
    );
}
