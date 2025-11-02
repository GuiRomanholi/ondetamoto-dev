package br.com.fiap.ondetamoto.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testGetAuthorities_Admin() {
        Usuario usuario = new Usuario("admin@teste.com", "123", UserRole.ADMIN);

        List<? extends GrantedAuthority> authorities = (List<? extends GrantedAuthority>) usuario.getAuthorities();

        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testGetAuthorities_User() {
        Usuario usuario = new Usuario("user@teste.com", "123", UserRole.USER);

        List<? extends GrantedAuthority> authorities = (List<? extends GrantedAuthority>) usuario.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.get(0).getAuthority());
    }

    @Test
    void testGettersAndSetters() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@teste.com");
        usuario.setSenha("senha123");
        usuario.setRole(UserRole.ADMIN);

        assertEquals("email@teste.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
        assertEquals(UserRole.ADMIN, usuario.getRole());
    }
}
