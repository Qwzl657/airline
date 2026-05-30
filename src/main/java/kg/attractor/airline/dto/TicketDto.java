package kg.attractor.airline.dto;

import kg.attractor.airline.model.SeatClass;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {
    private Long id;
    private String seatNumber;
    private SeatClass seatClass;
    private Double price;
    private Boolean booked;
}