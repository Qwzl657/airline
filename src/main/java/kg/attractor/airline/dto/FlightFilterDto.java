package kg.attractor.airline.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightFilterDto {
    private String departureCity;
    private String arrivalCity;
    private String dateFrom;
    private String dateTo;
}