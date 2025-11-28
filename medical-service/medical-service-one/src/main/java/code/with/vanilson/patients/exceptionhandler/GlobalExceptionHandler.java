package code.with.vanilson.patients.exceptionhandler;

import code.with.vanilson.patients.exception.InvalidEmailException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.ProblemDetail;

@RestControllerAdvice("code.with.vanilson")
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEmailException.class)
    public ProblemDetail handleInvalidEmailException(
            InvalidEmailException exception) {
        return exception.toProblemDetail();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(
            IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(
                BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllExceptions(Exception exception) {
        return ErrorResponse
                .builder(exception, INTERNAL_SERVER_ERROR,
                        exception.getMessage())
                .build();
    }
}
