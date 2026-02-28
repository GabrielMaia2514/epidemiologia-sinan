package com.seuprojeto.epidemiologia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "populacao_estado",
        uniqueConstraints = @UniqueConstraint(columnNames = {"estado_id", "ano"}))
public class PopulacaoEstado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    private int ano;

    private Long populacao;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public Long getPopulacao() { return populacao; }
    public void setPopulacao(Long populacao) { this.populacao = populacao; }
}
