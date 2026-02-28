package com.seuprojeto.epidemiologia.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "indicadores",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_indicador",
                        columnNames = {"estado_id", "doenca_id", "ano", "mes"}
                )
        }
)
public class Indicador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 🔹 Relacionamento com Estado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    // 🔹 Relacionamento com Doenca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doenca_id", nullable = false)
    private Doenca doenca;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false)
    private Integer mes;

    @Column(nullable = false)
    private Integer casos;

    private Integer obitos;

    @Column(precision = 10, scale = 4)
    private BigDecimal taxaPor100k;

    public Integer getId() {
        return id;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Doenca getDoenca() {
        return doenca;
    }

    public void setDoenca(Doenca doenca) {
        this.doenca = doenca;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês deve estar entre 1 e 12");
        }
        this.mes = mes;
    }

    public Integer getCasos() {
        return casos;
    }

    public void setCasos(Integer casos) {
        this.casos = casos;
    }

    public Integer getObitos() {
        return obitos;
    }

    public void setObitos(Integer obitos) {
        this.obitos = obitos;
    }

    public BigDecimal getTaxaPor100k() {
        return taxaPor100k;
    }

    public void setTaxaPor100k(BigDecimal taxaPor100k) {
        this.taxaPor100k = taxaPor100k;
    }
}
