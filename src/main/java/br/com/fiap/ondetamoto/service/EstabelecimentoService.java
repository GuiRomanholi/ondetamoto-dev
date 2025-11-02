package br.com.fiap.ondetamoto.service;

import br.com.fiap.ondetamoto.controller.EstabelecimentoController;
import br.com.fiap.ondetamoto.dto.EstabelecimentoRequest;
import br.com.fiap.ondetamoto.dto.EstabelecimentoResponse;
import br.com.fiap.ondetamoto.model.Estabelecimento;
import br.com.fiap.ondetamoto.model.Usuario;
import br.com.fiap.ondetamoto.repository.EstabelecimentoRepository;
import br.com.fiap.ondetamoto.repository.UsuarioRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException; // Importe esta exceção

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EstabelecimentoService {
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final UsuarioRepository usuarioRepository;

    public EstabelecimentoService(EstabelecimentoRepository estabelecimentoRepository, UsuarioRepository usuarioRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public Page<Estabelecimento> findAllForWeb(Pageable pageable) {
        return estabelecimentoRepository.findAll(pageable);
    }

    public Optional<Estabelecimento> findByIdForWeb(Long id) {
        return estabelecimentoRepository.findById(id);
    }

    public Estabelecimento saveForWeb(Estabelecimento estabelecimento) {
        return estabelecimentoRepository.save(estabelecimento);
    }

    @CacheEvict(value = {"estabelecimentosWeb", "estabelecimentoWeb", "estabelecimentosApi", "estabelecimentoApi"}, allEntries = true)
    public void deleteByIdForWeb(Long id) {
        if (!estabelecimentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Estabelecimento não encontrado com o ID: " + id);
        }
        estabelecimentoRepository.deleteById(id);
    }

    @Cacheable(value = "estabelecimentos", key = "#id")
    public EstabelecimentoResponse findByIdForApi(Long id) {
        Estabelecimento est = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com o ID: " + id));
        return estabelecimentoToResponse(est, false);
    }

    public Page<EstabelecimentoResponse> findAllForApi(Pageable pageable) {
        return findAllForWeb(pageable)
                .map(estabelecimento -> estabelecimentoToResponse(estabelecimento, true));
    }

    public EstabelecimentoResponse createForApi(EstabelecimentoRequest estabelecimentoRequest) {
        Estabelecimento estabelecimento = requestToEstabelecimento(estabelecimentoRequest);
        Estabelecimento estabelecimentoSalvo = estabelecimentoRepository.save(estabelecimento);
        return estabelecimentoToResponse(estabelecimentoSalvo, false);
    }

    @CachePut(value = "estabelecimentos", key = "#id")
    public EstabelecimentoResponse updateForApi(Long id, EstabelecimentoRequest request) {
        Estabelecimento estExistente = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com o ID: " + id));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + request.getUsuarioId()));

        estExistente.setEndereco(request.getEndereco());
        estExistente.setUsuario(usuario);

        Estabelecimento salvo = estabelecimentoRepository.save(estExistente);
        return estabelecimentoToResponse(salvo, false);
    }

    @CacheEvict(value = {"estabelecimentosWeb", "estabelecimentoWeb", "estabelecimentosApi", "estabelecimentoApi"}, allEntries = true)
    public void deleteByIdForApi(Long id) {
        deleteByIdForWeb(id);
    }


    private Estabelecimento requestToEstabelecimento(EstabelecimentoRequest estabelecimentoRequest) {
        Usuario usuario = usuarioRepository.findById(estabelecimentoRequest.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + estabelecimentoRequest.getUsuarioId()));

        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setEndereco(estabelecimentoRequest.getEndereco());
        estabelecimento.setUsuario(usuario);

        return estabelecimento;
    }

    private EstabelecimentoResponse estabelecimentoToResponse(Estabelecimento estabelecimento, boolean self) {
        Link link;
        if (self) {
            link = linkTo(methodOn(EstabelecimentoController.class).readEstabelecimento(estabelecimento.getId())).withSelfRel();
        } else {
            link = linkTo(methodOn(EstabelecimentoController.class).readEstabelecimentos(0)).withRel("Lista de Estabelecimentos");
        }

        String usuarioEmail = (estabelecimento.getUsuario() != null) ? estabelecimento.getUsuario().getEmail() : null;

        return new EstabelecimentoResponse(estabelecimento.getId(), estabelecimento.getEndereco(), usuarioEmail, link);
    }
}