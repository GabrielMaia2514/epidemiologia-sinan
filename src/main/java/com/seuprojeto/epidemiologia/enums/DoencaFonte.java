package com.seuprojeto.epidemiologia.enums;

public enum DoencaFonte {
    DENGUE("dengue", "https://s3.sa-east-1.amazonaws.com/ckan.saude.gov.br/SINAN/Dengue/csv/DENGBR"),
    CHIKUNGUNYA("chikungunya", "https://s3.sa-east-1.amazonaws.com/ckan.saude.gov.br/SINAN/Chikungunya/csv/CHIKBR");

    private final String nome;
    private final String baseUrl;

    DoencaFonte(String nome, String baseUrl) {
        this.nome = nome;
        this.baseUrl = baseUrl;
    }

    public String getNome() {
        return nome;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static DoencaFonte fromNome(String nome) {
        for (DoencaFonte d : values()) {
            if (d.nome.equalsIgnoreCase(nome)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Doença não suportada: " + nome);
    }
}