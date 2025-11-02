package br.com.fiap.ondetamoto.controller.web;

import br.com.fiap.ondetamoto.model.Estabelecimento;
import br.com.fiap.ondetamoto.repository.UsuarioRepository;
import br.com.fiap.ondetamoto.service.EstabelecimentoService; // <-- IMPORTE O SERVIÇO
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/estabelecimentos")
public class EstabelecimentoWebController {

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/listar")
    public String listarEstabelecimentos(Model model) {
        Page<Estabelecimento> estabelecimentos = estabelecimentoService.findAllForWeb(PageRequest.of(0, 10));
        model.addAttribute("estabelecimentos", estabelecimentos);
        return "estabelecimento/listar_estabelecimentos";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("estabelecimento", new Estabelecimento());
        model.addAttribute("allUsuarios", usuarioRepository.findAll());
        return "estabelecimento/form_estabelecimento";
    }

    @PostMapping("/salvar")
    public String salvarEstabelecimento(@Valid @ModelAttribute("estabelecimento") Estabelecimento estabelecimento, RedirectAttributes redirectAttributes) {
        try {
            estabelecimentoService.saveForWeb(estabelecimento);
            redirectAttributes.addFlashAttribute("mensagem", "Estabelecimento salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao salvar o estabelecimento: " + e.getMessage());
        }
        return "redirect:/estabelecimentos/listar";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Estabelecimento> estabelecimentoOptional = estabelecimentoService.findByIdForWeb(id);
        if (estabelecimentoOptional.isPresent()) {
            model.addAttribute("estabelecimento", estabelecimentoOptional.get());
            model.addAttribute("allUsuarios", usuarioRepository.findAll());
            return "estabelecimento/form_estabelecimento";
        } else {
            // Adiciona uma mensagem de erro se não encontrar
            // redirectAttributes.addFlashAttribute("erro", "Estabelecimento não encontrado.");
            return "redirect:/estabelecimentos/listar";
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirEstabelecimento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            estabelecimentoService.deleteByIdForWeb(id);
            redirectAttributes.addFlashAttribute("mensagem", "Estabelecimento excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao excluir o estabelecimento: " + e.getMessage());
        }
        return "redirect:/estabelecimentos/listar";
    }
}