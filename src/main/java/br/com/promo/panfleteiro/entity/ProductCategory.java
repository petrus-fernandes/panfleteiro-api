package br.com.promo.panfleteiro.entity;

public enum ProductCategory {
    BUTCHER(1, "Açougue"),
    COLD_CUTS(2, "Frios"),
    DAIRY(3, "Laticínios"),
    CELLAR(4, "Adega"),
    DRINKS(5, "Bebidas"),
    HYGIENE(6, "Higiene"),
    CLEANING(7, "Limpeza"),
    VEGETABLES(8, "Hortifruti"),
    GROCERY(9, "Mercearia"),
    BAKERY(10, "Padaria"),
    CANNED_GOODS(11, "Enlatados"),
    GRAINS(12, "Cereais"),
    ROTISSERIE(13, "Rotisseria"),
    PETSHOP(14, "Petshop"),
    FISH(15, "Peixaria"),
    AUTO_PARTS(16, "Auto-peças"),

    FROZEN(17, "Congelados");

    final private Integer code;

    final private String name;

    ProductCategory(Integer code, String name) {
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

    public static ProductCategory fromName(String name) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.name.equals(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid name for ProductCategory: " + name);
    }
}
