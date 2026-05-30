// Источник: конспект ЧАСТЬ 2.4 — фильтрация, Pageable
// Даты строками — парсинг в сервисе

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