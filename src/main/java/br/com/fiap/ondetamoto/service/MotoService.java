package br.com.fiap.ondetamoto.service;

import br.com.fiap.ondetamoto.controller.EstabelecimentoController;
import br.com.fiap.ondetamoto.controller.MotoController;
import br.com.fiap.ondetamoto.controller.SetoresController;
import br.com.fiap.ondetamoto.dto.EstabelecimentoResponse;
import br.com.fiap.ondetamoto.dto.MotoRequest;
import br.com.fiap.ondetamoto.dto.MotoResponse;
import br.com.fiap.ondetamoto.dto.SetoresResponse;
import br.com.fiap.ondetamoto.model.Moto;
import br.com.fiap.ondetamoto.model.Setores;
import br.com.fiap.ondetamoto.repository.MotoRepository;
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
public class MotoService {
    private final MotoRepository motoRepository;
    private final SetoresRepository setoresRepository;

    public MotoService(MotoRepository motoRepository, SetoresRepository setoresRepository){
        this.motoRepository = motoRepository;
        this.setoresRepository = setoresRepository;
    }

    @Cacheable(value = "motosWeb", key = "#pageable.pageNumber")
    public Page<Moto> findAllForWeb(Pageable pageable) {
        return motoRepository.findAll(pageable);
    }

    @Cacheable(value = "motoWeb", key = "#id")
    public Optional<Moto> findByIdForWeb(Long id) {
        return motoRepository.findById(id);
    }

    @CacheEvict(value = {"motosWeb", "motoWeb", "motosApi", "motoApi"}, allEntries = true)
    public Moto saveForWeb(Moto moto) {
        if (moto.getSetores() != null && moto.getSetores().getId() != null) {
            Setores setor = setoresRepository.findById(moto.getSetores().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado com o ID: " + moto.getSetores().getId()));
            moto.setSetores(setor);
        } else {
            moto.setSetores(null);
        }
        return motoRepository.save(moto);
    }

    @CacheEvict(value = {"motosWeb", "motoWeb", "motosApi", "motoApi"}, allEntries = true)
    public void deleteByIdForWeb(Long id) {
        if (!motoRepository.existsById(id)) {
            throw new EntityNotFoundException("Moto não encontrada com o ID: " + id);
        }
        motoRepository.deleteById(id);
    }

    @Cacheable(value = "motosApi", key = "#pageable.pageNumber")
    public Page<MotoResponse> findAllForApi(Pageable pageable) {
        return motoRepository.findAll(pageable)
                .map(moto -> motoToResponse(moto, true));
    }

    @Cacheable(value = "motoApi", key = "#id")
    public MotoResponse findByIdForApi(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com o ID: " + id));
        return motoToResponse(moto, false);
    }

    @CacheEvict(value = {"motosWeb", "motosApi"}, allEntries = true)
    public MotoResponse createForApi(MotoRequest motoRequest) {
        Moto moto = requestToMoto(motoRequest);
        Moto motoSalva = motoRepository.save(moto);
        return motoToResponse(motoSalva, false);
    }

    @CachePut(value = "motoApi", key = "#id")
    @CacheEvict(value = {"motosWeb", "motosApi"}, allEntries = true)
    public MotoResponse updateForApi(Long id, MotoRequest motoRequest) {
        Moto motoExistente = motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com o ID: " + id));

        motoExistente.setMarca(motoRequest.getMarca());
        motoExistente.setPlaca(motoRequest.getPlaca());
        motoExistente.setTag(motoRequest.getTag());

        if (motoRequest.getIdSetores() != null) {
            Setores setor = setoresRepository.findById(motoRequest.getIdSetores())
                    .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado com o ID: " + motoRequest.getIdSetores()));
            motoExistente.setSetores(setor);
        } else {
            motoExistente.setSetores(null);
        }

        Moto motoAtualizada = motoRepository.save(motoExistente);
        return motoToResponse(motoAtualizada, true);
    }

    @CacheEvict(value = {"motosWeb", "motoWeb", "motosApi", "motoApi"}, allEntries = true)
    public void deleteByIdForApi(Long id) {
        deleteByIdForWeb(id);
    }

    public Page<MotoResponse> findByTagForApi(String tag, Pageable pageable) {
        Page<Moto> motos = motoRepository.findByTagIgnoreCase(tag, pageable);
        return motos.map(moto -> motoToResponse(moto, true));
    }

    public Page<MotoResponse> findBySetorIdForApi(Long setorId, Pageable pageable) {
        Page<Moto> motos = motoRepository.findBySetoresId(setorId, pageable);
        return motos.map(moto -> motoToResponse(moto, true));
    }


    private Moto requestToMoto(MotoRequest motoRequest) {
        Moto moto = new Moto();
        moto.setMarca(motoRequest.getMarca());
        moto.setPlaca(motoRequest.getPlaca());
        moto.setTag(motoRequest.getTag());

        if (motoRequest.getIdSetores() != null) {
            Setores setores = setoresRepository.findById(motoRequest.getIdSetores())
                    .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado com o ID: " + motoRequest.getIdSetores()));
            moto.setSetores(setores);
        }
        return moto;
    }

    private MotoResponse motoToResponse(Moto moto, boolean self) {
        Link link = self ?
                linkTo(methodOn(MotoController.class).readMoto(moto.getId())).withSelfRel() :
                linkTo(methodOn(MotoController.class).readMotos(0)).withRel("Lista de Motos");

        SetoresResponse setoresResponse = null;
        if (moto.getSetores() != null) {
            var setor = moto.getSetores();
            var estabelecimento = setor.getEstabelecimento();
            EstabelecimentoResponse estabelecimentoResponse = null;
            if (estabelecimento != null) {
                String usuarioEmail = (estabelecimento.getUsuario() != null) ? estabelecimento.getUsuario().getEmail() : null;
                estabelecimentoResponse = new EstabelecimentoResponse(
                        estabelecimento.getId(),
                        estabelecimento.getEndereco(),
                        usuarioEmail,
                        linkTo(methodOn(EstabelecimentoController.class).readEstabelecimento(estabelecimento.getId())).withSelfRel()
                );
            }
            setoresResponse = new SetoresResponse(
                    setor.getId(), setor.getNome(), setor.getTipo(), setor.getTamanho(), estabelecimentoResponse,
                    linkTo(methodOn(SetoresController.class).readSetor(setor.getId())).withSelfRel()
            );
        }

        return new MotoResponse(moto.getId(), moto.getPlaca(), moto.getMarca(), moto.getTag(), setoresResponse, link);
    }
}