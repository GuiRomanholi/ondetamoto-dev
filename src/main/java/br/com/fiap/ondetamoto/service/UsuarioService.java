package br.com.fiap.ondetamoto.service;

import br.com.fiap.ondetamoto.dto.RegisterDTO;
import br.com.fiap.ondetamoto.dto.UsuarioRequest;
import br.com.fiap.ondetamoto.exception.EmailAlreadyExistsException;
import br.com.fiap.ondetamoto.model.UserRole;
import br.com.fiap.ondetamoto.model.Usuario;
import br.com.fiap.ondetamoto.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "usuariosPage", key = "#pageable.pageNumber")
    public Page<Usuario> findAllForWeb(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Cacheable(value = "usuario", key = "#id")
    public Usuario findByIdForWeb(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    @Transactional
    @CachePut(value = "usuario", key = "#result.id")
    @CacheEvict(value = {"usuariosPage", "usuariosList"}, allEntries = true)
    public Usuario saveForWeb(Usuario usuario) {

        if (usuario.getId() != null) {
            Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

            if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
                if (!passwordEncoder.matches(usuario.getSenha(), usuarioExistente.getSenha())) {
                    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
                } else {
                    usuario.setSenha(usuarioExistente.getSenha());
                }
            } else {
                usuario.setSenha(usuarioExistente.getSenha());
            }
        } else {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    @CacheEvict(value = {"usuario", "usuariosPage", "usuariosList"}, allEntries = true)
    public void deleteByIdForWeb(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Cacheable(value = "usuariosList")
    public List<Usuario> findAllForApi() {
        return usuarioRepository.findAll();
    }

    public Usuario findByIdForApi(Long id) {
        return findByIdForWeb(id);
    }

    @Transactional
    @CachePut(value = "usuario", key = "#id")
    @CacheEvict(value = {"usuariosPage", "usuariosList"}, allEntries = true)
    public Usuario updateForApi(Long id, UsuarioRequest request) {
        Usuario usuarioExistente = findByIdForApi(id);

        usuarioExistente.setEmail(request.getEmail());
        usuarioExistente.setRole(UserRole.valueOf(request.getRole().toUpperCase()));

        if (request.getSenha() != null && !request.getSenha().isEmpty()) {
            if (!passwordEncoder.matches(request.getSenha(), usuarioExistente.getSenha())) {
                usuarioExistente.setSenha(passwordEncoder.encode(request.getSenha()));
            }
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    @CacheEvict(value = {"usuario", "usuariosPage", "usuariosList"}, allEntries = true)
    public void deleteByIdForApi(Long id) {
        deleteByIdForWeb(id);
    }


    @Transactional
    @CachePut(value = "usuario", key = "#result.id")
    @CacheEvict(value = {"usuariosPage", "usuariosList"}, allEntries = true)
    public Usuario registerUser(RegisterDTO data) {
        if (usuarioRepository.findByEmail(data.email()) != null) {
            throw new EmailAlreadyExistsException("Este e-mail já está cadastrado.");
        }
        String encryptedPassword = passwordEncoder.encode(data.senha());
        UserRole userRoleEnum = UserRole.valueOf(data.role().toUpperCase());
        Usuario newUser = new Usuario(data.email(), encryptedPassword, userRoleEnum);

        return usuarioRepository.save(newUser);
    }

}