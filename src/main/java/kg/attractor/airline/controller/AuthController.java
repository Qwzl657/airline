package kg.attractor.airline.controller;

import jakarta.validation.Valid;
import kg.attractor.airline.dto.UserCreateDto;
import kg.attractor.airline.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userCreateDto", new UserCreateDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("userCreateDto") UserCreateDto dto,
            BindingResult errors,
            Model model) {

        if (errors.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(dto);
            log.info("Зарегистрирован пользователь: {}", dto.getEmail());
            return "redirect:/auth/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("emailError", e.getMessage());
            return "auth/register";
        }
    }
}