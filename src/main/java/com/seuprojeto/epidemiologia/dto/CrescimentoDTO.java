package com.seuprojeto.epidemiologia.dto;

import java.math.BigDecimal;

public class CrescimentoDTO {
    private Integer ano;
    private Integer totalCasos;
    private BigDecimal percentualCrescimento;

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

    public BigDecimal getPercentualCrescimento() {
        return percentualCrescimento;
    }

    public void setPercentualCrescimento(BigDecimal percentualCrescimento) {
        this.percentualCrescimento = percentualCrescimento;
    }
    public CrescimentoDTO(Integer ano, Integer totalCasos, BigDecimal percentualCrescimento) {
        this.ano = ano;
        this.totalCasos = totalCasos;
        this.percentualCrescimento = percentualCrescimento;
    }
}
