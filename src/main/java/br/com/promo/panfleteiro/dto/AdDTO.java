package br.com.promo.panfleteiro.dto;

public class AdDTO {
    
    private Long id;

    private MarketDTO mercado;

    private String nome;

    private Double preco;

    private String url;

    private String distancia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MarketDTO getMercado() {
        return mercado;
    }

    public void setMercado(MarketDTO mercado) {
        this.mercado = mercado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

}
