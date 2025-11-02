package br.com.fiap.ondetamoto.service;

import br.com.fiap.ondetamoto.dto.UsuarioRequest;
import br.com.fiap.ondetamoto.model.UserRole;
import br.com.fiap.ondetamoto.model.Usuario;
import br.com.fiap.ondetamoto.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByIdForApi_Sucesso() {
        Usuario usuario = new Usuario("email@teste.com", "senha", UserRole.USER);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findByIdForApi(1L);

        assertNotNull(result);
        assertEquals("email@teste.com", result.getEmail());
    }

    @Test
    void testFindByIdForApi_NotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.findByIdForApi(1L));
    }

    @Test
    void testUpdateForApi() {
        Usuario usuarioExistente = new Usuario("old@teste.com", "senhaAntigaCriptografada", UserRole.USER);

        UsuarioRequest request = new UsuarioRequest();
        request.setEmail("novo@teste.com");
        request.setSenha("SenhaNova1");
        request.setRole("ADMIN");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.matches("SenhaNova1", usuarioExistente.getSenha())).thenReturn(false);
        when(passwordEncoder.encode("SenhaNova1")).thenReturn("senhaNovaCriptografada");
        when(usuarioRepository.save(usuarioExistente)).thenReturn(usuarioExistente);

        Usuario atualizado = usuarioService.updateForApi(1L, request);

        assertEquals("novo@teste.com", atualizado.getEmail());
        assertEquals(UserRole.ADMIN, atualizado.getRole());
        assertEquals("senhaNovaCriptografada", atualizado.getSenha());
    }

}
