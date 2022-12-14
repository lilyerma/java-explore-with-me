package explorewithme.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
@Slf4j
@ControllerAdvice

public class ErrorHandler {

    @ExceptionHandler(ArgumentException.class)
    public Error handleNotCorrectInput(ArgumentException ex) {
        log.debug("For the requested operation the conditions are not met.");
        return new Error(new ArrayList<>(), ex.getMessage(),
                "For the requested operation the conditions are not met.", HttpStatus.FORBIDDEN, LocalDateTime.now());
    }

    // 400 — если ошибка валидации: ValidationException
    @ExceptionHandler(ValidationException.class)
    public Error handleNotCorrectValidate(ValidationException exception) {
        log.debug("For the requested operation the conditions are not met.");
        return new Error(new ArrayList<>(), exception.getMessage(),
                "For the requested operation the conditions are not met.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    // 404 — для всех ситуаций, если искомый объект не найден
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(NotFoundException exception) {
        log.debug("The required object was not found.");
        return new Error(exception.getMessage(),
                "The required object was not found.", HttpStatus.NOT_FOUND, LocalDateTime.now());
    }

    // 500 - для ситуаций конфликта существующих значений
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleConflictException(ConflictException exception) {
        log.debug("Integrity constraint has been violated");
        return new Error(exception.getMessage(),
                "Integrity constraint has been violated", HttpStatus.CONFLICT, LocalDateTime.now());
    }


}