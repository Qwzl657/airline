package kg.attractor.airline.repository;

import kg.attractor.airline.model.SeatClass;
import kg.attractor.airline.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByFlightId(Long flightId);

    List<Ticket> findByFlightIdAndSeatClass(Long flightId, SeatClass seatClass);

    long countByFlightId(Long flightId);

    List<Ticket> findByFlightIdAndSeatClassAndBooked(Long flightId, SeatClass seatClass, Boolean booked);
}