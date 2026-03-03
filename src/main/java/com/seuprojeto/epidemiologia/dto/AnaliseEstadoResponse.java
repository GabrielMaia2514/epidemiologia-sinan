package com.seuprojeto.epidemiologia.dto;

import java.math.BigDecimal;
import java.util.List;

public class AnaliseEstadoResponse {
    private String estado;
    private String doenca;

    private Integer piorAno;
    private Integer totalPiorAno;

    private List<SerieTemporalDTO> serieTemporal;

    private List<CrescimentoDTO> crescimentoAnual;

    private BigDecimal maiorTaxaPor100k;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDoenca() {
        return doenca;
    }

    public void setDoenca(String doenca) {
        this.doenca = doenca;
    }

    public Integer getPiorAno() {
        return piorAno;
    }

    public void setPiorAno(Integer piorAno) {
        this.piorAno = piorAno;
    }

    public Integer getTotalPiorAno() {
        return totalPiorAno;
    }

    public void setTotalPiorAno(Integer totalPiorAno) {
        this.totalPiorAno = totalPiorAno;
    }

    public List<SerieTemporalDTO> getSerieTemporal() {
        return serieTemporal;
    }

    public void setSerieTemporal(List<SerieTemporalDTO> serieTemporal) {
        this.serieTemporal = serieTemporal;
    }

    public List<CrescimentoDTO> getCrescimentoAnual() {
        return crescimentoAnual;
    }

    public void setCrescimentoAnual(List<CrescimentoDTO> crescimentoAnual) {
        this.crescimentoAnual = crescimentoAnual;
    }

    public BigDecimal getMaiorTaxaPor100k() {
        return maiorTaxaPor100k;
    }

    public void setMaiorTaxaPor100k(BigDecimal maiorTaxaPor100k) {
        this.maiorTaxaPor100k = maiorTaxaPor100k;
    }
}
