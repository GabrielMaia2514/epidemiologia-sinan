package com.seuprojeto.epidemiologia.service;
import com.seuprojeto.epidemiologia.dto.AnaliseEstadoResponse;
import com.seuprojeto.epidemiologia.dto.CrescimentoDTO;
import com.seuprojeto.epidemiologia.dto.SerieTemporalDTO;
import com.seuprojeto.epidemiologia.entity.Estado;
import com.seuprojeto.epidemiologia.entity.Doenca;
import com.seuprojeto.epidemiologia.exception.RecursoNaoEncontradoException;
import com.seuprojeto.epidemiologia.repository.EstadoRepository;
import com.seuprojeto.epidemiologia.repository.DoencaRepository;
import com.seuprojeto.epidemiologia.repository.IndicadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnaliseService {
    private final EstadoRepository estadoRepository;
    private final DoencaRepository doencaRepository;
    private final IndicadorRepository indicadorRepository;

    public AnaliseEstadoResponse analisarEstadoPorAno(String uf, String nomeDoenca) {
        Estado estado = buscarEstado(uf);
        Doenca doenca = buscarDoenca(nomeDoenca);

        AnaliseEstadoResponse response = inicializarResponse(estado, doenca);

        List<Object[]> serie = indicadorRepository.buscarSerieTemporal(estado.getId(), doenca.getId());
        if (serie == null || serie.isEmpty()) {
            return response;
        }

        List<SerieTemporalDTO> serieTemporal = montarSerieTemporal(serie);
        response.setSerieTemporal(serieTemporal);

        definirPiorAno(response, serieTemporal);
        response.setCrescimentoAnual(calcularCrescimento(serieTemporal));
        response.setMaiorTaxaPor100k(calcularMaiorTaxa(serieTemporal));

        return response;
    }

    private Estado buscarEstado(String uf) {
        return estadoRepository.findBySigla(uf.trim().toUpperCase())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estado não encontrado: " + uf));
    }

    private Doenca buscarDoenca(String nomeDoenca) {
        return doencaRepository.findByNome(nomeDoenca.trim())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Doença não encontrada: " + nomeDoenca));
    }

    private AnaliseEstadoResponse inicializarResponse(Estado estado, Doenca doenca) {
        AnaliseEstadoResponse response = new AnaliseEstadoResponse();
        response.setEstado(estado.getNome());
        response.setDoenca(doenca.getNome());
        response.setSerieTemporal(List.of());
        response.setCrescimentoAnual(List.of());
        response.setMaiorTaxaPor100k(BigDecimal.ZERO);
        return response;
    }

    private List<SerieTemporalDTO> montarSerieTemporal(List<Object[]> serie) {
        return serie.stream()
                .map(obj -> {
                    Number anoNumero = (Number) obj[0];
                    Number totalNumero = (Number) obj[1];
                    BigDecimal taxa = obj.length > 2 && obj[2] != null
                            ? converterParaBigDecimal(obj[2])
                            : BigDecimal.ZERO;

                    return new SerieTemporalDTO(
                            anoNumero != null ? anoNumero.intValue() : 0,
                            totalNumero != null ? totalNumero.intValue() : 0,
                            taxa
                    );
                })
                .toList();
    }

    private BigDecimal converterParaBigDecimal(Object valor) {
        if (valor instanceof BigDecimal bd) {
            return bd;
        }
        if (valor instanceof Number num) {
            return BigDecimal.valueOf(num.doubleValue());
        }
        return BigDecimal.ZERO;
    }

    private void definirPiorAno(AnaliseEstadoResponse response, List<SerieTemporalDTO> serieTemporal) {
        serieTemporal.stream()
                .max(Comparator.comparing(SerieTemporalDTO::getTotalCasos))
                .ifPresent(pior -> {
                    response.setPiorAno(pior.getAno());
                    response.setTotalPiorAno(pior.getTotalCasos());
                });
    }

    private List<CrescimentoDTO> calcularCrescimento(List<SerieTemporalDTO> serieTemporal) {
        List<CrescimentoDTO> crescimento = new ArrayList<>();
        for (int i = 1; i < serieTemporal.size(); i++) {
            SerieTemporalDTO anterior = serieTemporal.get(i - 1);
            SerieTemporalDTO atual = serieTemporal.get(i);

            if (anterior.getTotalCasos() > 0) {
                BigDecimal crescimentoPercentual =
                        BigDecimal.valueOf(atual.getTotalCasos() - anterior.getTotalCasos())
                                .divide(BigDecimal.valueOf(anterior.getTotalCasos()), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100));

                crescimento.add(new CrescimentoDTO(atual.getAno(), atual.getTotalCasos(), crescimentoPercentual));
            }
        }
        return crescimento;
    }

    private BigDecimal calcularMaiorTaxa(List<SerieTemporalDTO> serieTemporal) {
        return serieTemporal.stream()
                .map(SerieTemporalDTO::getTaxaPor100k)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

}
