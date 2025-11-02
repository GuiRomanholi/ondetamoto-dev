package br.com.fiap.ondetamoto.model;

public enum Tipo {
    MANUTENCAO("Manutencao"),
    USAVEL("Usaveis"),
    QUEBRADA("Quebradas");

    private String descricao;

    Tipo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
