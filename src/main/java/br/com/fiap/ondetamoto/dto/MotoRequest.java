package br.com.fiap.ondetamoto.dto;

import br.com.fiap.ondetamoto.model.Estabelecimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class MotoRequest {

    @NotNull(message = "A marca é obrigatória")
    private String marca;
    @NotBlank(message = "A placa é obrigatória")
    @Pattern(regexp = "^[A-Z0-9]{1,7}$",
            message = "A placa deve conter apenas letras maiúsculas e números, com até 7 caracteres."
    )
    private String placa;
    private String tag;

    // Adicione esta linha
    private Long idSetores;


    public MotoRequest(){
    }

    public MotoRequest(String marca, String placa, String tag){
        this.marca = marca;
        this.placa = placa;
        this.tag = tag;
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

    public Long getIdSetores() {
        return idSetores;
    }

    public void setIdSetores(Long idSetores) {
        this.idSetores = idSetores;
    }
}
