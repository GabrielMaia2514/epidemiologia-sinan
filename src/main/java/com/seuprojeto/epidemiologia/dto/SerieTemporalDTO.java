package com.seuprojeto.epidemiologia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SerieTemporalDTO {
    private Integer ano;
    private Integer totalCasos;
    private BigDecimal taxaPor100k;

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getTotalCasos() {
        return totalCasos;
    }

    public void setTotalCasos(Integer totalCasos) {
        this.totalCasos = totalCasos;
    }
}
