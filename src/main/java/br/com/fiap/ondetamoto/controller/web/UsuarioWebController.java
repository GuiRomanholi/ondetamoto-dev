package br.com.fiap.ondetamoto.controller.web;

import br.com.fiap.ondetamoto.model.Usuario;
// REMOVIDO: UsuarioRepository
import br.com.fiap.ondetamoto.service.UsuarioService; // IMPORTADO
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
// REMOVIDO: PasswordEncoder
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        Page<Usuario> usuarios = usuarioService.findAllForWeb(PageRequest.of(0, 10));
        model.addAttribute("usuarios", usuarios);
        return "usuario/listar_usuarios";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/form_usuario";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.saveForWeb(usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao salvar o usuário: " + e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.findByIdForWeb(id);
            model.addAttribute("usuario", usuario);
            return "usuario/form_usuario";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("erro", "Usuário não encontrado.");
            return "redirect:/usuarios/listar";
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteByIdForWeb(id);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao excluir o usuário: " + e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }
}