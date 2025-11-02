package br.com.fiap.ondetamoto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EstabelecimentoRequest {

    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    private Long usuarioId;

    public EstabelecimentoRequest() {
    }

    public EstabelecimentoRequest(String endereco, Long usuarioId) {
        this.endereco = endereco;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}