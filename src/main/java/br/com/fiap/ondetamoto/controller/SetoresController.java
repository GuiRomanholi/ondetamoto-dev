package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.dto.SetoresRequest;
import br.com.fiap.ondetamoto.dto.SetoresResponse;
import br.com.fiap.ondetamoto.service.SetoresService;
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
@RequestMapping(value = "/api/setores", produces = {"application/json"})
@Tag(name = "api-setores")
public class SetoresController {

    @Autowired
    private SetoresService setoresService;

    @Operation(summary = "Criar um novo Setor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Setor cadastrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SetoresResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Atributos informados são inválidos ou dependência não encontrada",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity<SetoresResponse> createSetores(@Valid @RequestBody SetoresRequest setoresRequest) {
        try {
            SetoresResponse response = setoresService.createForApi(setoresRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            // Se o estabelecimento não for encontrado, retorna 400 Bad Request
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Retorna uma lista de setores")
    @GetMapping
    public ResponseEntity<Page<SetoresResponse>> readSetores(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("nome").ascending());
        Page<SetoresResponse> response = setoresService.findAllForApi(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Retorna um setor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setor encontrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SetoresResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum setor encontrado",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SetoresResponse> readSetor(@PathVariable Long id) {
        try {
            SetoresResponse response = setoresService.findByIdForApi(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Atualiza um setor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setor encontrado e atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SetoresResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum setor ou estabelecimento encontrado para atualizar",
                    content = @Content(schema = @Schema()))
    })
    @PutMapping("/{id}")
    public ResponseEntity<SetoresResponse> updateSetor(@PathVariable Long id, @Valid @RequestBody SetoresRequest setoresRequest) {
        try {
            SetoresResponse response = setoresService.updateForApi(id, setoresRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Exclui um setor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Nenhum setor encontrado para excluir",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "204", description = "Setor excluído com sucesso",
                    content = @Content(schema = @Schema()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetor(@PathVariable Long id) {
        try {
            setoresService.deleteByIdForApi(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}