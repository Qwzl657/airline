package kg.attractor.airline.service.impl;

import kg.attractor.airline.dto.FlightCreateDto;
import kg.attractor.airline.dto.FlightDto;
import kg.attractor.airline.dto.TicketDto;
import kg.attractor.airline.exception.FlightNotFoundException;
import kg.attractor.airline.exception.InvalidFlightTimeException;
import kg.attractor.airline.exception.UserNotFoundException;
import kg.attractor.airline.model.*;
import kg.attractor.airline.repository.FlightRepository;
import kg.attractor.airline.repository.TicketRepository;
import kg.attractor.airline.repository.UserRepository;
import kg.attractor.airline.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public Page<FlightDto> searchFlights(String departureCity, String arrivalCity,
                                          String dateFrom, String dateTo,
                                          Pageable pageable) {
        LocalDateTime from = null;
        LocalDateTime to   = null;

        if (dateFrom != null && !dateFrom.isBlank()) {
            try {
                from = LocalDate.parse(dateFrom).atStartOfDay();
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Некорректный формат даты вылета");
            }
        }
        if (dateTo != null && !dateTo.isBlank()) {
            try {
                to = LocalDate.parse(dateTo).atTime(23, 59, 59);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Некорректный формат даты прилёта");
            }
        }

        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "Дата начала не может быть позже даты конца");
        }

        String dep = (departureCity != null && !departureCity.isBlank())
                ? departureCity : null;
        String arr = (arrivalCity != null && !arrivalCity.isBlank())
                ? arrivalCity : null;

        return flightRepository.findByFilter(dep, arr, from, to, pageable)
                .map(this::toDto);
    }

    @Override
    public FlightDto getById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(
                        "Рейс не найден: " + id));
        return toDtoWithTickets(flight);
    }

    @Override
    @Transactional
    public void createFlight(FlightCreateDto dto, String email) {
        User company = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "Компания не найдена: " + email));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime departureTime;
        LocalDateTime arrivalTime;

        try {
            departureTime = LocalDateTime.parse(dto.getDepartureTime(), fmt);
            arrivalTime   = LocalDateTime.parse(dto.getArrivalTime(), fmt);
        } catch (DateTimeParseException e) {
            throw new InvalidFlightTimeException(
                    "Некорректный формат даты/времени. Ожидается: yyyy-MM-ddTHH:mm");
        }

        if (departureTime.isBefore(LocalDateTime.now())) {
            throw new InvalidFlightTimeException(
                    "Время вылета не может быть в прошлом");
        }

        if (!arrivalTime.isAfter(departureTime)) {
            throw new InvalidFlightTimeException(
                    "Время прилёта должно быть позже времени вылета");
        }

        if (arrivalTime.isAfter(departureTime.plusHours(48))) {
            throw new InvalidFlightTimeException(
                    "Продолжительность рейса не может превышать 48 часов");
        }

        if (dto.getDepartureCity().trim().equalsIgnoreCase(dto.getArrivalCity().trim())) {
            throw new InvalidFlightTimeException(
                    "Город вылета и прилёта не могут совпадать");
        }

        String flightNumber = generateFlightNumber(company.getFullName());

        Flight flight = Flight.builder()
                .flightNumber(flightNumber)
                .company(company)
                .departureCity(dto.getDepartureCity().trim())
                .arrivalCity(dto.getArrivalCity().trim())
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .build();
        flightRepository.save(flight);

        generateTickets(flight);

        log.info("Создан рейс: {} компания: {}", flightNumber, company.getFullName());
    }

    @Override
    public Page<FlightDto> getCompanyFlights(String email, Pageable pageable) {
        User company = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "Компания не найдена: " + email));
        return flightRepository.findByCompanyId(company.getId(), pageable)
                .map(this::toDto);
    }

    private String generateFlightNumber(String companyName) {
        String prefix = companyName
                .replaceAll("[^A-Za-z]", "")
                .toUpperCase();
        prefix = prefix.substring(0, Math.min(3, prefix.length()));
        String suffix = UUID.randomUUID().toString()
                .substring(0, 4).toUpperCase();
        return prefix + "-" + suffix;
    }

    private void generateTickets(Flight flight) {
        List<Ticket> tickets = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            tickets.add(Ticket.builder()
                    .flight(flight)
                    .seatNumber("A" + i)
                    .seatClass(SeatClass.ECONOMY)
                    .price(generatePrice(SeatClass.ECONOMY))
                    .booked(false)
                    .build());
        }
        for (int i = 1; i <= 3; i++) {
            tickets.add(Ticket.builder()
                    .flight(flight)
                    .seatNumber("B" + i)
                    .seatClass(SeatClass.BUSINESS)
                    .price(generatePrice(SeatClass.BUSINESS))
                    .booked(false)
                    .build());
        }
        ticketRepository.saveAll(tickets);
    }

    private Double generatePrice(SeatClass seatClass) {
        if (seatClass == SeatClass.ECONOMY) {
            return 200.0 + Math.round(Math.random() * 300);
        }
        return 700.0 + Math.round(Math.random() * 500);
    }

    private FlightDto toDto(Flight flight) {
        List<Ticket> tickets = ticketRepository.findByFlightId(flight.getId());

        long freeEconomy  = tickets.stream()
                .filter(t -> t.getSeatClass() == SeatClass.ECONOMY && !t.getBooked())
                .count();
        long freeBusiness = tickets.stream()
                .filter(t -> t.getSeatClass() == SeatClass.BUSINESS && !t.getBooked())
                .count();
        Double minPrice = tickets.stream()
                .filter(t -> !t.getBooked())
                .mapToDouble(Ticket::getPrice)
                .min()
                .orElse(0.0);

        return FlightDto.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .companyName(flight.getCompany().getFullName())
                .companyLogo(flight.getCompany().getLogo())
                .departureCity(flight.getDepartureCity())
                .arrivalCity(flight.getArrivalCity())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .minPrice(minPrice)
                .freeEconomy(freeEconomy)
                .freeBusiness(freeBusiness)
                .build();
    }

    private FlightDto toDtoWithTickets(Flight flight) {
        List<Ticket> tickets = ticketRepository.findByFlightId(flight.getId());

        List<TicketDto> ticketDtos = tickets.stream()
                .map(t -> TicketDto.builder()
                        .id(t.getId())
                        .seatNumber(t.getSeatNumber())
                        .seatClass(t.getSeatClass())
                        .price(t.getPrice())
                        .booked(t.getBooked())
                        .build())
                .toList();

        long freeEconomy  = tickets.stream()
                .filter(t -> t.getSeatClass() == SeatClass.ECONOMY && !t.getBooked())
                .count();
        long freeBusiness = tickets.stream()
                .filter(t -> t.getSeatClass() == SeatClass.BUSINESS && !t.getBooked())
                .count();
        Double minPrice = tickets.stream()
                .filter(t -> !t.getBooked())
                .mapToDouble(Ticket::getPrice)
                .min()
                .orElse(0.0);

        return FlightDto.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .companyName(flight.getCompany().getFullName())
                .companyLogo(flight.getCompany().getLogo())
                .departureCity(flight.getDepartureCity())
                .arrivalCity(flight.getArrivalCity())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .tickets(ticketDtos)
                .minPrice(minPrice)
                .freeEconomy(freeEconomy)
                .freeBusiness(freeBusiness)
                .build();
    }
}