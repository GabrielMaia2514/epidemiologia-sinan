package com.seuprojeto.epidemiologia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias_doenca")
public class CategoriaDoenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}