package br.com.fiap.ondetamoto.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Setores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    private String tamanho;

    @ManyToOne
    @JoinColumn(name = "id_estabelecimento")
    private Estabelecimento estabelecimento;

    @OneToMany(mappedBy = "setores")
    private List<Moto> motos;

    public Setores(Long id, String nome, String tamanho, Tipo tipo, Estabelecimento estabelecimento) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.estabelecimento = estabelecimento;
    }

    public Setores() {
    }


    // Getters e Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public List<Moto> getMotos() {
        return motos;
    }

    public void setMotos(List<Moto> motos) {
        this.motos = motos;
    }
}
