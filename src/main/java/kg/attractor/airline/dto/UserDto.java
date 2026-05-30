package kg.attractor.airline.dto;

import kg.attractor.airline.model.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private Role role;
    private String logo;
    private Boolean enabled;
    private Long bookingCount;
}