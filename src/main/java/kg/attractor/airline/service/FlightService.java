package kg.attractor.airline.service;

import kg.attractor.airline.dto.FlightCreateDto;
import kg.attractor.airline.dto.FlightDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightService {

    Page<FlightDto> searchFlights(String departureCity, String arrivalCity,
                                   String dateFrom, String dateTo,
                                   Pageable pageable);

    FlightDto getById(Long id);

    void createFlight(FlightCreateDto dto, String email);

    Page<FlightDto> getCompanyFlights(String email, Pageable pageable);
}