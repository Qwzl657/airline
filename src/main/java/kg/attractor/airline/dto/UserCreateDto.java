package kg.attractor.airline.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль минимум 6 символов")
    private String password;

    @NotBlank(message = "Полное имя обязательно")
    @Size(min = 2, max = 200, message = "Имя от 2 до 200 символов")
    private String fullName;
}