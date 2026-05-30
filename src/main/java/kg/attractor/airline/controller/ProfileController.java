package kg.attractor.airline.controller;

import kg.attractor.airline.dto.BookingDto;
import kg.attractor.airline.dto.UserDto;
import kg.attractor.airline.service.BookingService;
import kg.attractor.airline.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping
    public String profile(Model model, Authentication auth) {
        UserDto user = userService.getCurrentUser(auth.getName());
        List<BookingDto> bookings = bookingService.getUserBookings(auth.getName());

        model.addAttribute("user", user);
        model.addAttribute("bookings", bookings);
        return "profile/profile";
    }

    @PostMapping("/logo")
    public String uploadLogo(@RequestParam("logo") MultipartFile file,
                             Authentication auth) throws IOException {

        if (file.isEmpty()) {
            return "redirect:/profile?error=emptyFile";
        }

        String originalName = file.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : ".png";

        if (!extension.matches("\\.(png|jpg|jpeg|gif)")) {
            return "redirect:/profile?error=invalidFormat";
        }

        String fileName = UUID.randomUUID() + extension;

        Path uploadDir = Paths.get("uploads/logos");
        Files.createDirectories(uploadDir);
        Files.copy(file.getInputStream(),
                uploadDir.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING);

        userService.updateLogo(auth.getName(), fileName);
        return "redirect:/profile";
    }
}