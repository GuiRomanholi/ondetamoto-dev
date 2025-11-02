package br.com.fiap.ondetamoto.repository;

import br.com.fiap.ondetamoto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca um usuario no banco de dados pelo email.
    UserDetails findByEmail(String email);
}
