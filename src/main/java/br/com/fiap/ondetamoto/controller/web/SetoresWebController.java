package br.com.fiap.ondetamoto.controller.web;

import br.com.fiap.ondetamoto.model.Setores;
import br.com.fiap.ondetamoto.repository.EstabelecimentoRepository;
import br.com.fiap.ondetamoto.service.SetoresService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/setores")
public class SetoresWebController {

    @Autowired
    private SetoresService setoresService;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository; // Mantido para popular o formulário

    @GetMapping("/listar")
    public String listarSetores(Model model) {
        Page<Setores> setores = setoresService.findAllForWeb(PageRequest.of(0, 10));
        model.addAttribute("setores", setores);
        return "setores/listar_setores";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        model.addAttribute("setor", new Setores());
        model.addAttribute("estabelecimentos", estabelecimentoRepository.findAll());
        return "setores/form_setores";
    }

    @PostMapping("/salvar")
    public String salvarSetor(@Valid @ModelAttribute("setor") Setores setor,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("estabelecimentos", estabelecimentoRepository.findAll());
            return "setores/form_setores";
        }

        try {
            setoresService.saveForWeb(setor); // Lógica de salvar agora está totalmente no serviço
            redirectAttributes.addFlashAttribute("mensagem", "Setor salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao salvar o setor: " + e.getMessage());
            model.addAttribute("estabelecimentos", estabelecimentoRepository.findAll());
            return "setores/form_setores";
        }

        return "redirect:/setores/listar";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Setores> setorOptional = setoresService.findByIdForWeb(id);
        if (setorOptional.isPresent()) {
            model.addAttribute("setor", setorOptional.get());
            model.addAttribute("estabelecimentos", estabelecimentoRepository.findAll());
            return "setores/form_setores";
        } else {
            redirectAttributes.addFlashAttribute("erro", "Setor não encontrado.");
            return "redirect:/setores/listar";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluirSetor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            setoresService.deleteByIdForWeb(id);
            redirectAttributes.addFlashAttribute("mensagem", "Setor excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro ao excluir o setor: " + e.getMessage());
        }
        return "redirect:/setores/listar";
    }
}