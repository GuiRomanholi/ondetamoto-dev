package br.com.fiap.ondetamoto.dto;

import br.com.fiap.ondetamoto.model.Estabelecimento;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioRequest {

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,25}$",
            message = "A senha deve ter entre 8 e 25 caracteres, conter ao menos uma letra maiúscula e um número, e não pode ter caracteres especiais.")
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @NotBlank(message = "A role é obrigatória")
    private String role;

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
