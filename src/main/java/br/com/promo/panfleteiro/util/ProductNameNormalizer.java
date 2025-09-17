package br.com.promo.panfleteiro.util;

import java.util.AbstractMap;
import java.util.List;
import java.util.regex.Pattern;

public class ProductNameNormalizer {

    private static final List<AbstractMap.SimpleEntry<Pattern, String>> BASE_RULES = List.of(
            entry("\\bMac\\b", "Macarrão"),
            entry("\\bMolho Tom\\b", "Molho de Tomate"),
            entry("\\bTrad\\b", "Tradicional"),
            entry("\\bC/", "Com"),
            entry("\\bCx\\b", "Caixa"),
            entry("\\bAmac\\b", "Amaciante"),
            entry("\\bAlim\\b", "Alimento"),
            entry("\\bConc?\\b", "Concentrado"),
            entry("\\bLeite Cond?\\b", "Leite Condensado"),
            entry("\\bRefrig?\\b", "Refrigerante"),
            entry("\\bLing\\b", "Linguiça"),
            entry("\\bPapel Hig\\b", "Papel Higiênico"),
            entry("\\bLt\\b", "Lata"),
            entry("\\bRefres\\b", "Refresco")
    );

    private static AbstractMap.SimpleEntry<Pattern, String> entry(String regex, String replacement) {
        return new AbstractMap.SimpleEntry<>(Pattern.compile(regex), replacement);
    }

    public static String normalize(String rawName) {
        if (rawName == null || rawName.isBlank()) return rawName;

        String result = rawName.trim();

        for (var rule : BASE_RULES) {
            result = rule.getKey().matcher(result).replaceAll(rule.getValue());
        }

        return capitalizeFirst(result);
    }

    private static String capitalizeFirst(String text) {
        if (text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

