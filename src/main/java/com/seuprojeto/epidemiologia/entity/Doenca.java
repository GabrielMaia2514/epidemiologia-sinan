package com.seuprojeto.epidemiologia.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "doencas")
public class Doenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nome;

    // Relacionamento com CategoriaDoenca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private CategoriaDoenca categoria;

    public Integer getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaDoenca getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDoenca categoria) {
        this.categoria = categoria;
    }
}

