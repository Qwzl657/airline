package kg.attractor.airline.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATE_LONG_FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter DATE_FULL_FMT =
            DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter DATETIME_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter DATETIME_LONG_FMT =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

    public String getDepartureTimeFormatted() {
        return departureTime != null ? departureTime.format(TIME_FMT) : "";
    }

    public String getArrivalTimeFormatted() {
        return arrivalTime != null ? arrivalTime.format(TIME_FMT) : "";
    }

    public String getDepartureDateShort() {
        return departureTime != null ? departureTime.format(DATE_LONG_FMT) : "";
    }

    public String getDepartureDateLong() {
        return departureTime != null ? departureTime.format(DATE_FULL_FMT) : "";
    }

    public String getArrivalDateLong() {
        return arrivalTime != null ? arrivalTime.format(DATE_FULL_FMT) : "";
    }

    public String getDepartureDatetime() {
        return departureTime != null ? departureTime.format(DATETIME_FMT) : "";
    }

    public String getArrivalDatetime() {
        return arrivalTime != null ? arrivalTime.format(DATETIME_FMT) : "";
    }
}