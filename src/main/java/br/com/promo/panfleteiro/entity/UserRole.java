package br.com.promo.panfleteiro.entity;

public enum UserRole {

    ADMIN("admin"),
    USER("user"),

    CLIENT("client");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
