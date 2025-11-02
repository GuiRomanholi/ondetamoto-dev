package br.com.fiap.ondetamoto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Moto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marca;
    @NotBlank(message = "A placa é obrigatória")
    private String placa;
    private String tag;

    @ManyToOne
    @JoinColumn(name = "id_setores")
    private Setores setores;

    public Moto(){
    }

    public Moto(Long id, String marca, String placa, String tag){
        this.id = id;
        this.marca = marca;
        this.placa = placa;
        this.tag = tag;
    }

    // Getters e Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Setores getSetores() {
        return setores;
    }

    public void setSetores(Setores setores) {
        this.setores = setores;
    }
}
