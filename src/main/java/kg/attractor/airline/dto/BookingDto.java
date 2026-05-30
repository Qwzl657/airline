package kg.attractor.airline.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String seatNumber;
    private String seatClass;
    private Double price;
    private LocalDateTime bookedAt;
    private String companyName;
    private String companyLogo;
}