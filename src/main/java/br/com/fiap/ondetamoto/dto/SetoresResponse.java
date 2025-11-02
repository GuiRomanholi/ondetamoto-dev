package br.com.fiap.ondetamoto.dto;

import br.com.fiap.ondetamoto.model.Estabelecimento;
import br.com.fiap.ondetamoto.model.Tipo;
import org.springframework.hateoas.Link;

public record SetoresResponse(Long id, String nome, Tipo tipo, String tamanho, EstabelecimentoResponse idEstabelecimento, Link link) {
}