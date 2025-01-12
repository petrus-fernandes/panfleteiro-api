package br.com.promo.panfleteiro.entity;

public enum ProductCategory {
    BUTCHER(1, "Açougue"),
    COLD_CUTS_AND_DAIRY(2, "Frios e Laticínios"),
    CELLAR_AND_DRINKS(3, "Adega e Bebidas"),
    HYGIENE_AND_CLEANING(4, "Higiene e Limpeza"),
    VEGETABLES_AND_GROCERY(5, "Hortifruti e Mercearia"),
    BAKERY(6, "Padaria"),
    CANNED_GOODS(7, "Enlatados"),
    GRAINS(8, "Cereais"),
    ROTISSERIE(9, "Rotisseria");

    final private Integer code;

    final private String name;

    private ProductCategory(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ProductCategory fromCode(int code) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.code == code) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid code for ProductCategory: " + code);
    }
}
