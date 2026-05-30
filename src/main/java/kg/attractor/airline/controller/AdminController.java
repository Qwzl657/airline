package kg.attractor.airline.controller;

import jakarta.validation.Valid;
import kg.attractor.airline.dto.CompanyCreateDto;
import kg.attractor.airline.dto.UserDto;
import kg.attractor.airline.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String dashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDto> companies = userService.getCompanies(pageable);

        model.addAttribute("companies", companies);
        if (!model.containsAttribute("companyCreateDto")) {
            model.addAttribute("companyCreateDto", new CompanyCreateDto());
        }
        return "admin/dashboard";
    }

    @PostMapping("/companies")
    public String createCompany(
            @Valid @ModelAttribute("companyCreateDto") CompanyCreateDto dto,
            BindingResult errors,
            Model model) {

        if (errors.hasErrors()) {
            Page<UserDto> companies = userService.getCompanies(
                    PageRequest.of(0, 10));
            model.addAttribute("companies", companies);
            return "admin/dashboard";
        }

        try {
            userService.createCompany(dto);
            return "redirect:/admin";
        } catch (IllegalArgumentException e) {
            model.addAttribute("companyError", e.getMessage());
            Page<UserDto> companies = userService.getCompanies(
                    PageRequest.of(0, 10));
            model.addAttribute("companies", companies);
            return "admin/dashboard";
        }
    }

    @PostMapping("/companies/{id}/toggle")
    public String toggleFreeze(
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        try {
            userService.toggleFreeze(id);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("freezeError", e.getMessage());
        }
        return "redirect:/admin";
    }
}