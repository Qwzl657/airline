package kg.attractor.airline.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightDto {
    private Long id;
    private String flightNumber;
    private String companyName;
    private String companyLogo;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private List<TicketDto> tickets;
    private Double minPrice;
    private long freeEconomy;
    private long freeBusiness;
}