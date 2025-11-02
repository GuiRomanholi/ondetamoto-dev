package br.com.fiap.ondetamoto.dto;

import org.springframework.hateoas.Link;

public record EstabelecimentoResponse(Long id, String endereco, String usuarioEmail, Link link) {
}
