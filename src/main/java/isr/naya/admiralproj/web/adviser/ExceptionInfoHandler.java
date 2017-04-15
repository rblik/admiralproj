package isr.naya.admiralproj.web.adviser;

import com.itextpdf.text.ExceptionConverter;
import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.exception.TimeOverlappingException;
import isr.naya.admiralproj.exception.TimeRangeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionInfoHandler {

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY) // 422
    @ExceptionHandler(NotFoundException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorInfoDTO notFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public ErrorInfoDTO conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, (Exception) e.getCause().getCause(), false);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(TimeRangeException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public ErrorInfoDTO timeRangeError(HttpServletRequest req, TimeRangeException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(ExceptionConverter.class)
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public ErrorInfoDTO pdfError(HttpServletRequest req, ExceptionConverter e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT) // 409
    @ExceptionHandler(TimeOverlappingException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public ErrorInfoDTO timeOverlappingError(HttpServletRequest req, TimeOverlappingException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(BindException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public ErrorInfoDTO bindValidationError(HttpServletRequest req, BindingResult result) {
        return logAndGetValidationErrorInfo(req, result);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public ErrorInfoDTO missingRequestParameterError(HttpServletRequest req, MissingServletRequestParameterException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public ErrorInfoDTO restValidationError(HttpServletRequest req, MethodArgumentNotValidException e) {
        return logAndGetValidationErrorInfo(req, e.getBindingResult());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(Exception.class)
    public ErrorInfoDTO handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }

    private ErrorInfoDTO logAndGetValidationErrorInfo(HttpServletRequest req, BindingResult result) {
        String[] details = result.getFieldErrors().stream()
                .map(fe -> fe.getField() + ' ' + fe.getDefaultMessage()).toArray(String[]::new);

        log.warn("Validation exception at request " + req.getRequestURL() + ": " + Arrays.toString(details));
        return new ErrorInfoDTO(req.getRequestURL(), "ValidationException", details);
    }

    private ErrorInfoDTO logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException) {
        if (logException) {
            log.error("Exception at request " + req.getRequestURL(), e);
        } else {
            log.warn("Exception at request " + req.getRequestURL() + ": " + e.toString());
        }
        return new ErrorInfoDTO(req.getRequestURL(), e);
    }
}
