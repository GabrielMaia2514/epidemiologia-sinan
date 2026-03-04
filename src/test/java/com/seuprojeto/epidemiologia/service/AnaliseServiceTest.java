package com.seuprojeto.epidemiologia.service;

import com.seuprojeto.epidemiologia.dto.AnaliseEstadoResponse;
import com.seuprojeto.epidemiologia.dto.SerieTemporalDTO;
import com.seuprojeto.epidemiologia.entity.Doenca;
import com.seuprojeto.epidemiologia.entity.Estado;
import com.seuprojeto.epidemiologia.repository.DoencaRepository;
import com.seuprojeto.epidemiologia.repository.EstadoRepository;
import com.seuprojeto.epidemiologia.repository.IndicadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AnaliseServiceTest {

    private static final String UF = "SC";
    private static final String NOME_ESTADO = "Santa Catarina";
    private static final Integer ESTADO_ID = 27;

    private static final String DOENCA = "dengue";
    private static final Integer DOENCA_ID = 1;


    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private DoencaRepository doencaRepository;

    @Mock
    private IndicadorRepository indicadorRepository;

    @InjectMocks
    private AnaliseService analiseService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Estado criarEstado() {
        Estado estado = new Estado();
        estado.setId(ESTADO_ID);
        estado.setNome(NOME_ESTADO);
        estado.setSigla(UF);
        return estado;
    }

    private Doenca criarDoenca() {
        Doenca doenca = new Doenca();
        doenca.setId(DOENCA_ID);
        doenca.setNome(DOENCA);
        return doenca;
    }

    private void mockEstadoEDoenca() {
        when(estadoRepository.findBySigla(UF))
                .thenReturn(Optional.of(criarEstado()));

        when(doencaRepository.findByNome(DOENCA))
                .thenReturn(Optional.of(criarDoenca()));
    }


    @Test
    void quandoNaoHouverDados_retornaResponseComListasVazias() {

        mockEstadoEDoenca();

        when(indicadorRepository.buscarSerieTemporal(ESTADO_ID, DOENCA_ID))
                .thenReturn(List.of());

        AnaliseEstadoResponse response =
                analiseService.analisarEstadoPorAno(UF, DOENCA);

        assertNotNull(response);
        assertEquals(NOME_ESTADO, response.getEstado());
        assertEquals(DOENCA, response.getDoenca());
        assertTrue(response.getSerieTemporal().isEmpty());
        assertTrue(response.getCrescimentoAnual().isEmpty());
        assertEquals(BigDecimal.ZERO, response.getMaiorTaxaPor100k());
    }

    @Test
    void quandoHouverDados_calculaIndicadoresCorretamente() {

        mockEstadoEDoenca();

        List<Object[]> dados = List.of(
                new Object[]{2020, 100L, BigDecimal.valueOf(10)},
                new Object[]{2021, 200L, BigDecimal.valueOf(20)},
                new Object[]{2022, 50L, BigDecimal.valueOf(5)}
        );

        when(indicadorRepository.buscarSerieTemporal(ESTADO_ID, DOENCA_ID))
                .thenReturn(dados);

        AnaliseEstadoResponse response =
                analiseService.analisarEstadoPorAno(UF, DOENCA);

        assertNotNull(response);
        assertEquals(NOME_ESTADO, response.getEstado());
        assertEquals(DOENCA, response.getDoenca());

        // Série temporal
        List<SerieTemporalDTO> serie = response.getSerieTemporal();
        assertEquals(3, serie.size());
        assertEquals(2020, serie.get(0).getAno());
        assertEquals(100, serie.get(0).getTotalCasos());

        // Pior ano (maior número de casos)
        assertEquals(2021, response.getPiorAno());

        // Maior taxa
        assertEquals(BigDecimal.valueOf(20), response.getMaiorTaxaPor100k());

        // Crescimento anual
        assertEquals(2, response.getCrescimentoAnual().size());

        BigDecimal crescimentoEsperado =
                BigDecimal.valueOf(100.00).setScale(2);

        assertEquals(
                crescimentoEsperado,
                response.getCrescimentoAnual()
                        .get(0)
                        .getPercentualCrescimento()
                        .setScale(2)
        );
    }
}
