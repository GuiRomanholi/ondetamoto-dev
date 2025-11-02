package br.com.fiap.ondetamoto.dto;

import org.springframework.hateoas.Link;

public record MotoResponse(Long id, String placa, String marca, String tag, SetoresResponse setores,Link link) {
}

