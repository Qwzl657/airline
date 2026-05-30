package kg.attractor.airline.error;

import kg.attractor.airline.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        log.warn("Пользователь не найден: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", 404);
        return "errors/error";
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public String handleFlightNotFound(FlightNotFoundException ex, Model model) {
        log.warn("Рейс не найден: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", 404);
        return "errors/error";
    }

    @ExceptionHandler(TicketAlreadyBookedException.class)
    public String handleTicketBooked(TicketAlreadyBookedException ex, Model model) {
        log.warn("Попытка повторного бронирования: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", 409);
        return "errors/error";
    }

    @ExceptionHandler(CompanyFreezeException.class)
    public String handleCompanyFreeze(CompanyFreezeException ex, Model model) {
        log.warn("Ошибка заморозки компании: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", 409);
        return "errors/error";
    }

    @ExceptionHandler(InvalidFlightTimeException.class)
    public String handleInvalidTime(InvalidFlightTimeException ex, Model model) {
        log.warn("Некорректное время рейса: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", 400);
        return "errors/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Непредвиденная ошибка: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "Что-то пошло не так. Попробуйте позже.");
        model.addAttribute("errorCode", 500);
        return "errors/error";
    }
}