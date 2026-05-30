package kg.attractor.airline.service.impl;

import kg.attractor.airline.dto.BookingDto;
import kg.attractor.airline.exception.TicketAlreadyBookedException;
import kg.attractor.airline.exception.TicketNotFoundException;
import kg.attractor.airline.exception.UserNotFoundException;
import kg.attractor.airline.model.Booking;
import kg.attractor.airline.model.Ticket;
import kg.attractor.airline.model.User;
import kg.attractor.airline.repository.BookingRepository;
import kg.attractor.airline.repository.TicketRepository;
import kg.attractor.airline.repository.UserRepository;
import kg.attractor.airline.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void book(Long ticketId, String email) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(
                        "Билет не найден: " + ticketId));

        if (ticket.getBooked()) {
            throw new TicketAlreadyBookedException(
                    "Место " + ticket.getSeatNumber() + " уже занято");
        }

        if (!ticket.getFlight().getCompany().isEnabled()) {
            throw new IllegalStateException(
                    "Нельзя бронировать билеты замороженной компании");
        }

        if (ticket.getFlight().getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(
                    "Нельзя забронировать билет на уже вылетевший рейс");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "Пользователь не найден: " + email));

        ticket.setBooked(true);
        ticketRepository.save(ticket);

        Booking booking = Booking.builder()
                .user(user)
                .ticket(ticket)
                .bookedAt(LocalDateTime.now())
                .build();
        bookingRepository.save(booking);

        log.info("Пользователь {} забронировал место {} рейс {}",
                email,
                ticket.getSeatNumber(),
                ticket.getFlight().getFlightNumber());
    }

    @Override
    public List<BookingDto> getUserBookings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "Пользователь не найден: " + email));

        return bookingRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Long getFlightIdByTicketId(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .map(t -> t.getFlight().getId())
                .orElse(1L);
    }

    private BookingDto toDto(Booking booking) {
        Ticket ticket = booking.getTicket();
        return BookingDto.builder()
                .id(booking.getId())
                .flightNumber(ticket.getFlight().getFlightNumber())
                .departureCity(ticket.getFlight().getDepartureCity())
                .arrivalCity(ticket.getFlight().getArrivalCity())
                .departureTime(ticket.getFlight().getDepartureTime())
                .arrivalTime(ticket.getFlight().getArrivalTime())
                .seatNumber(ticket.getSeatNumber())
                .seatClass(ticket.getSeatClass().name())
                .price(ticket.getPrice())
                .bookedAt(booking.getBookedAt())
                .companyName(ticket.getFlight().getCompany().getFullName())
                .companyLogo(ticket.getFlight().getCompany().getLogo())
                .build();
    }
}