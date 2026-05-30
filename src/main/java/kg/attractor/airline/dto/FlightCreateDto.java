package kg.attractor.airline.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightCreateDto {

    @NotBlank(message = "Город вылета обязателен")
    @Size(min = 2, max = 100, message = "Город от 2 до 100 символов")
    private String departureCity;

    @NotBlank(message = "Город прилёта обязателен")
    @Size(min = 2, max = 100, message = "Город от 2 до 100 символов")
    private String arrivalCity;

    @NotBlank(message = "Дата и время вылета обязательны")
    private String departureTime;

    @NotBlank(message = "Дата и время прилёта обязательны")
    private String arrivalTime;
}