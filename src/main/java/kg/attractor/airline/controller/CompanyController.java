package kg.attractor.airline.controller;

import jakarta.validation.Valid;
import kg.attractor.airline.dto.FlightCreateDto;
import kg.attractor.airline.dto.FlightDto;
import kg.attractor.airline.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final FlightService flightService;

    @GetMapping("/flights")
    public String companyFlights(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            Authentication auth) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("departureTime").ascending());

        Page<FlightDto> flightsPage =
                flightService.getCompanyFlights(auth.getName(), pageable);

        model.addAttribute("flightsPage", flightsPage);
        return "company/flights";
    }

    @GetMapping("/flights/new")
    public String newFlightForm(Model model) {
        model.addAttribute("flightCreateDto", new FlightCreateDto());
        return "company/flight_form";
    }

    @PostMapping("/flights/new")
    public String createFlight(
            @Valid @ModelAttribute("flightCreateDto") FlightCreateDto dto,
            BindingResult errors,
            Authentication auth,
            Model model) {

        if (errors.hasErrors()) {
            return "company/flight_form";
        }

        try {
            flightService.createFlight(dto, auth.getName());
            return "redirect:/company/flights";
        } catch (Exception e) {
            model.addAttribute("flightError", e.getMessage());
            return "company/flight_form";
        }
    }
}