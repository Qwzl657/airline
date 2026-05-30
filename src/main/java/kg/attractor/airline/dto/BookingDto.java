package kg.attractor.airline.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private static final DateTimeFormatter DATETIME_LONG_FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String getDepartureTimeFormatted() {
        return departureTime != null ? departureTime.format(DATETIME_LONG_FMT) : "";
    }

    public String getBookedAtFormatted() {
        return bookedAt != null ? bookedAt.format(DATE_FMT) : "";
    }
}