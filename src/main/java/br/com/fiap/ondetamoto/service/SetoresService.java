package br.com.fiap.ondetamoto.service;

import br.com.fiap.ondetamoto.controller.EstabelecimentoController;
import br.com.fiap.ondetamoto.controller.SetoresController;
import br.com.fiap.ondetamoto.dto.EstabelecimentoResponse;
import br.com.fiap.ondetamoto.dto.SetoresRequest;
import br.com.fiap.ondetamoto.dto.SetoresResponse;
import br.com.fiap.ondetamoto.model.Estabelecimento;
import br.com.fiap.ondetamoto.model.Setores;
import br.com.fiap.ondetamoto.repository.EstabelecimentoRepository;
import br.com.fiap.ondetamoto.repository.SetoresRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class SetoresService {
    private final SetoresRepository setoresRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;

    public SetoresService(SetoresRepository setoresRepository, EstabelecimentoRepository estabelecimentoRepository) {
        this.setoresRepository = setoresRepository;
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    @Cacheable(value = "setoresWeb", key = "#pageable.pageNumber")
    public Page<Setores> findAllForWeb(Pageable pageable) {
        return setoresRepository.findAll(pageable);
    }

    @Cacheable(value = "setorWeb", key = "#id")
    public Optional<Setores> findByIdForWeb(Long id) {
        return setoresRepository.findById(id);
    }

    @CacheEvict(value = {"setoresWeb", "setorWeb", "setoresApi", "setorApi"}, allEntries = true)
    public Setores saveForWeb(Setores setor) {
        if (setor.getEstabelecimento() != null && setor.getEstabelecimento().getId() != null) {
            Estabelecimento est = estabelecimentoRepository.findById(setor.getEstabelecimento().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com o ID: " + setor.getEstabelecimento().getId()));
            setor.setEstabelecimento(est);
        } else {
            setor.setEstabelecimento(null);
        }
        return setoresRepository.save(setor);
    }

    @CacheEvict(value = {"setoresWeb", "setorWeb", "setoresApi", "setorApi"}, allEntries = true)
    public void deleteByIdForWeb(Long id) {
        if (!setoresRepository.existsById(id)) {
            throw new EntityNotFoundException("Setor não encontrado com o ID: " + id);
        }
        setoresRepository.deleteById(id);
    }

    @Cacheable(value = "setoresApi", key = "#pageable.pageNumber")
    public Page<SetoresResponse> findAllForApi(Pageable pageable) {
        return setoresRepository.findAll(pageable)
                .map(setor -> setoresToResponse(setor, true));
    }

    @Cacheable(value = "setorApi", key = "#id")
    public SetoresResponse findByIdForApi(Long id) {
        Setores setor = setoresRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado com o ID: " + id));
        return setoresToResponse(setor, false);
    }

    @CacheEvict(value = {"setoresWeb", "setoresApi"}, allEntries = true)
    public SetoresResponse createForApi(SetoresRequest setoresRequest) {
        Setores setores = requestToSetores(setoresRequest);
        Setores setorSalvo = setoresRepository.save(setores);
        return setoresToResponse(setorSalvo, false);
    }

    @CachePut(value = "setorApi", key = "#id")
    @CacheEvict(value = {"setoresWeb", "setoresApi"}, allEntries = true)
    public SetoresResponse updateForApi(Long id, SetoresRequest request) {
        Setores setorExistente = setoresRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado com o ID: " + id));

        setorExistente.setNome(request.getNome());
        setorExistente.setTipo(request.getTipo());
        setorExistente.setTamanho(request.getTamanho());

        if (request.getIdEstabelecimento() != null) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(request.getIdEstabelecimento())
                    .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com o ID: " + request.getIdEstabelecimento()));
            setorExistente.setEstabelecimento(estabelecimento);
        } else {
            setorExistente.setEstabelecimento(null);
        }

        Setores setorAtualizado = setoresRepository.save(setorExistente);
        return setoresToResponse(setorAtualizado, false);
    }

    @CacheEvict(value = {"setoresWeb", "setorWeb", "setoresApi", "setorApi"}, allEntries = true)
    public void deleteByIdForApi(Long id) {
        deleteByIdForWeb(id);
    }

    private Setores requestToSetores(SetoresRequest setoresRequest) {
        Setores setores = new Setores();
        setores.setNome(setoresRequest.getNome());
        setores.setTipo(setoresRequest.getTipo());
        setores.setTamanho(setoresRequest.getTamanho());

        if (setoresRequest.getIdEstabelecimento() != null) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(setoresRequest.getIdEstabelecimento())
                    .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com o ID: " + setoresRequest.getIdEstabelecimento()));
            setores.setEstabelecimento(estabelecimento);
        }
        return setores;
    }

    private SetoresResponse setoresToResponse(Setores setores, boolean self) {
        Link link = self ?
                linkTo(methodOn(SetoresController.class).readSetor(setores.getId())).withSelfRel() :
                linkTo(methodOn(SetoresController.class).readSetores(0)).withRel("Lista de Setores");

        EstabelecimentoResponse estabelecimentoResponse = null;
        if (setores.getEstabelecimento() != null) {
            Estabelecimento est = setores.getEstabelecimento();
            Link estLink = linkTo(methodOn(EstabelecimentoController.class).readEstabelecimento(est.getId())).withSelfRel();
            String usuarioEmail = (est.getUsuario() != null) ? est.getUsuario().getEmail() : null;
            estabelecimentoResponse = new EstabelecimentoResponse(est.getId(), est.getEndereco(), usuarioEmail, estLink);
        }

        return new SetoresResponse(
                setores.getId(), setores.getNome(), setores.getTipo(), setores.getTamanho(), estabelecimentoResponse, link
        );
    }
}