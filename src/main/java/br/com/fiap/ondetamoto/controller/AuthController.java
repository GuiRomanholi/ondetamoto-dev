package br.com.fiap.ondetamoto.controller;

import br.com.fiap.ondetamoto.dto.AuthDTO;
import br.com.fiap.ondetamoto.dto.RegisterDTO;
import br.com.fiap.ondetamoto.exception.EmailAlreadyExistsException; // IMPORTANTE
import br.com.fiap.ondetamoto.model.Usuario;
import br.com.fiap.ondetamoto.service.TokenService;
import br.com.fiap.ondetamoto.service.UsuarioService; // IMPORTADO
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO authDTO) {
        var userPwd = new UsernamePasswordAuthenticationToken(
                authDTO.email(),
                authDTO.senha()
        );
        var auth = this.authenticationManager.authenticate(userPwd);
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO) {
        try {
            usuarioService.registerUser(registerDTO);
            return ResponseEntity.ok().build();

        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Role inválida. Use 'USER' ou 'ADMIN'.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ocorreu um erro inesperado.");
        }
    }

}