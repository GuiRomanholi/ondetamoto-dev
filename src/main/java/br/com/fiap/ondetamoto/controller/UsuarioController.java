package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.dto.UsuarioRequest;
import br.com.fiap.ondetamoto.model.Usuario;
import br.com.fiap.ondetamoto.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/usuario", produces = {"application/json"})
@Tag(name = "api-usuarios", description = "CRUD de Usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Retorna uma lista de Usuarios")
    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.ok(usuarioService.findAllForApi());
    }

    @Operation(summary = "Retorna um usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))}), // Corrigido
            @ApiResponse(responseCode = "404", description = "Nenhum usuario encontrado",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.findByIdForApi(id);
            return ResponseEntity.ok(usuario);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualiza um usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado e atualizado com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))}),
            @ApiResponse(responseCode = "404", description = "Nenhum usuario encontrado para atualizar",
                    content = @Content(schema = @Schema()))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody @Valid UsuarioRequest request) {
        try {
            Usuario atualizado = usuarioService.updateForApi(id, request);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Exclui um usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Nenhum usuario encontrado para excluir",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "204", description = "Usuario excluído com sucesso",
                    content = @Content(schema = @Schema()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            usuarioService.deleteByIdForApi(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}