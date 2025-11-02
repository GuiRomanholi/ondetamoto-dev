package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.dto.EstabelecimentoRequest;
import br.com.fiap.ondetamoto.dto.EstabelecimentoResponse;
import br.com.fiap.ondetamoto.service.EstabelecimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/estabelecimentos", produces = {"application/json"})
@Tag(name = "api-estabelecimentos")
public class EstabelecimentoController {

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @Operation(summary = "Criar um novo Estabelecimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estabelecimento cadastrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstabelecimentoResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Atributos informados são inválidos",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity<EstabelecimentoResponse> createEstabelecimento(@Valid @RequestBody EstabelecimentoRequest estabelecimentoRequest) {
        EstabelecimentoResponse response = estabelecimentoService.createForApi(estabelecimentoRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Retorna uma lista de estabelecimentos")
    @GetMapping
    public ResponseEntity<Page<EstabelecimentoResponse>> readEstabelecimentos(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("endereco").ascending());
        Page<EstabelecimentoResponse> response = estabelecimentoService.findAllForApi(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Retorna um estabelecimento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estabelecimento encontrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstabelecimentoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum estabelecimento encontrado",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponse> readEstabelecimento(@PathVariable Long id) {
        try {
            EstabelecimentoResponse response = estabelecimentoService.findByIdForApi(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Atualiza um estabelecimento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estabelecimento atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstabelecimentoResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum estabelecimento encontrado para atualizar",
                    content = @Content(schema = @Schema()))
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponse> updateEstabelecimento(@PathVariable Long id,
                                                                         @Valid @RequestBody EstabelecimentoRequest estabelecimentoRequest) {
        try {
            EstabelecimentoResponse response = estabelecimentoService.updateForApi(id, estabelecimentoRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Exclui um estabelecimento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Nenhum estabelecimento encontrado para excluir",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "204", description = "Estabelecimento excluído com sucesso",
                    content = @Content(schema = @Schema()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstabelecimento(@PathVariable Long id) {
        try {
            estabelecimentoService.deleteByIdForApi(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}