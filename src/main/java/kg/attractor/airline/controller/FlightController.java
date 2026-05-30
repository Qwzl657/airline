package kg.attractor.airline.controller;

import kg.attractor.airline.dto.FlightDto;
import kg.attractor.airline.service.BookingService;
import kg.attractor.airline.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;
    private final BookingService bookingService;

    @GetMapping({"/", "/flights"})
    public String flights(
            @RequestParam(required = false) String departureCity,
            @RequestParam(required = false) String arrivalCity,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            Authentication auth) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("departureTime").ascending());

        try {
            Page<FlightDto> flightsPage = flightService.searchFlights(
                    departureCity, arrivalCity, dateFrom, dateTo, pageable);
            model.addAttribute("flightsPage", flightsPage);
        } catch (IllegalArgumentException e) {
            model.addAttribute("searchError", e.getMessage());
            model.addAttribute("flightsPage",
                    Page.empty(pageable));
        }

        model.addAttribute("departureCity", departureCity);
        model.addAttribute("arrivalCity", arrivalCity);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);

        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("userRole",
                    auth.getAuthorities().iterator().next().getAuthority());
        }

        return "flights/list";
    }

    @GetMapping("/flights/{id}")
    public String flightDetail(@PathVariable Long id,
                               Model model,
                               Authentication auth) {
        FlightDto flight = flightService.getById(id);
        model.addAttribute("flight", flight);

        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("userRole",
                    auth.getAuthorities().iterator().next().getAuthority());
        }

        return "flights/detail";
    }

    @PostMapping("/bookings/{ticketId}")
    public String bookTicket(@PathVariable Long ticketId,
                             Authentication auth,
                             RedirectAttributes redirectAttrs) {
        try {
            bookingService.book(ticketId, auth.getName());
            redirectAttrs.addFlashAttribute("bookingSuccess",
                    "Место успешно забронировано!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("bookingError", e.getMessage());
            return "redirect:/flights/" + ticketId + "?error";
        }
        return "redirect:/profile";
    }
}