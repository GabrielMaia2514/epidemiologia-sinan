package com.seuprojeto.epidemiologia.utils;

import java.util.Map;

/**
 * Classe utilitária para constantes geográficas do Brasil.
 * Centraliza códigos IBGE para evitar repetição no código de negócio.
 */
public final class BrasilConstants {

    // Construtor privado para impedir que alguém dê "new BrasilConstants()"
    private BrasilConstants() {}

    private static final Map<String, String> IBGE_UF = Map.ofEntries(
            Map.entry("11", "RO"), Map.entry("12", "AC"), Map.entry("13", "AM"),
            Map.entry("14", "RR"), Map.entry("15", "PA"), Map.entry("16", "AP"),
            Map.entry("17", "TO"), Map.entry("21", "MA"), Map.entry("22", "PI"),
            Map.entry("23", "CE"), Map.entry("24", "RN"), Map.entry("25", "PB"),
            Map.entry("26", "PE"), Map.entry("27", "AL"), Map.entry("28", "SE"),
            Map.entry("29", "BA"), Map.entry("31", "MG"), Map.entry("32", "ES"),
            Map.entry("33", "RJ"), Map.entry("35", "SP"), Map.entry("41", "PR"),
            Map.entry("42", "SC"), Map.entry("43", "RS"), Map.entry("50", "MS"),
            Map.entry("51", "MT"), Map.entry("52", "GO"), Map.entry("53", "DF")
    );

    /**
     * Retorna a sigla do estado baseada no código IBGE.
     * @param codigo Código IBGE (ex: "35")
     * @return Sigla (ex: "SP") ou "??" se não encontrado.
     */
    public static String getSiglaPorCodigo(String codigo) {
        if (codigo == null) return "??";
        return IBGE_UF.getOrDefault(codigo, "??");
    }
}