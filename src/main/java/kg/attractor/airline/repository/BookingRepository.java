package kg.attractor.airline.repository;

import kg.attractor.airline.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.ticket.flight.company.id = :companyId
    """)
    boolean existsActiveBookingsByCompanyId(@Param("companyId") Long companyId);

    @Query("""
        SELECT COUNT(b) FROM Booking b
        WHERE b.ticket.flight.company.id = :companyId
    """)
    long countBookingsByCompanyId(@Param("companyId") Long companyId);
}