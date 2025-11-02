package br.com.fiap.ondetamoto.controller.web;

import br.com.fiap.ondetamoto.dto.RegisterDTO;
import br.com.fiap.ondetamoto.model.UserRole;
import br.com.fiap.ondetamoto.exception.EmailAlreadyExistsException; // Importe a exceção
import br.com.fiap.ondetamoto.service.UsuarioService;           // Importe o serviço
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/")
public class AuthWebController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Email ou senha inválidos.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("registerDTO")) {
            model.addAttribute("registerDTO", new RegisterDTO(null, null, null));
        }
        model.addAttribute("allowedRoles", Arrays.asList(UserRole.values()));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerDTO") RegisterDTO registerDTO,
                               RedirectAttributes redirectAttributes) {
        try {

            usuarioService.registerUser(registerDTO);

            redirectAttributes.addFlashAttribute("successMessage", "Usuário registrado com sucesso! Faça seu login.");
            return "redirect:/login";

        } catch (EmailAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            return "redirect:/register";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocorreu um erro inesperado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}