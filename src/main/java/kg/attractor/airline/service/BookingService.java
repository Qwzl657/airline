package kg.attractor.airline.service;

import kg.attractor.airline.dto.BookingDto;
import java.util.List;

public interface BookingService {

    void book(Long ticketId, String email);

    List<BookingDto> getUserBookings(String email);
}