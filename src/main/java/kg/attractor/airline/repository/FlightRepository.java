package kg.attractor.airline.repository;

import kg.attractor.airline.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("""
        SELECT f FROM Flight f
        WHERE (:departureCity IS NULL OR LOWER(f.departureCity) LIKE LOWER(CONCAT('%', :departureCity, '%')))
          AND (:arrivalCity   IS NULL OR LOWER(f.arrivalCity)   LIKE LOWER(CONCAT('%', :arrivalCity,   '%')))
          AND (:dateFrom      IS NULL OR f.departureTime       >= :dateFrom)
          AND (:dateTo        IS NULL OR f.departureTime       <= :dateTo)
    """)
    Page<Flight> findByFilter(
            @Param("departureCity") String departureCity,
            @Param("arrivalCity")   String arrivalCity,
            @Param("dateFrom")      LocalDateTime dateFrom,
            @Param("dateTo")        LocalDateTime dateTo,
            Pageable pageable
    );

    Page<Flight> findByCompanyId(Long companyId, Pageable pageable);
}