package kg.attractor.airline.controller;

import kg.attractor.airline.dto.FlightDto;
import kg.attractor.airline.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FlightApiController {

    private final FlightService flightService;

    @GetMapping("/flights")
    public Page<FlightDto> searchFlights(
            @RequestParam(required = false) String departureCity,
            @RequestParam(required = false) String arrivalCity,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("departureTime").ascending());

        return flightService.searchFlights(
                departureCity, arrivalCity, dateFrom, dateTo, pageable);
    }
}