package br.com.fiap.ondetamoto.dto;

import br.com.fiap.ondetamoto.model.Estabelecimento;
import br.com.fiap.ondetamoto.model.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SetoresRequest {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 80, message = "O nome deve ter entre 3 e 80 caracteres")
    private String nome;

    @NotNull(message = "O tipo é obrigatório")
    private Tipo tipo;

    @NotBlank(message = "O tamanho é obrigatório")
    private String tamanho;

    @NotNull(message = "O ID do estabelecimento é obrigatório")
    private Long idEstabelecimento;

    public SetoresRequest(){
    }

    public SetoresRequest(String nome, Tipo tipo, String tamanho, Long idEstabelecimento){
        this.nome = nome;
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.idEstabelecimento = idEstabelecimento;
    }

    // Getters e Setters

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

    public Long getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(Long idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }
}