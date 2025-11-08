package SmartCertify_backend.SmartCertify_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetails> handleUserException(UserException ex, WebRequest req) {
        ErrorDetails err = new ErrorDetails(
                ex.getMessage(),
                req.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CertificateException.class)
    public ResponseEntity<ErrorDetails> handleCertificateException(CertificateException ex, WebRequest req) {
        ErrorDetails err = new ErrorDetails(
                ex.getMessage(),
                req.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CourseException.class)
    public ResponseEntity<ErrorDetails> handleCourseException(CourseException ex, WebRequest req) {
        ErrorDetails err = new ErrorDetails(
                ex.getMessage(),
                req.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorDetails err = new ErrorDetails(message, "Validation Error", LocalDateTime.now());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetails> handleNoHandlerFound(NoHandlerFoundException ex, WebRequest req) {
        ErrorDetails err = new ErrorDetails(
                "Endpoint not found",
                req.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException ex, WebRequest req) {
        ErrorDetails err = new ErrorDetails(
                ex.getMessage(),
                req.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest req) {
        ErrorDetails err = new ErrorDetails(
                ex.getMessage(),
                req.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest req) {
        ErrorDetails err = new ErrorDetails(ex.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest req) {
        ErrorDetails err = new ErrorDetails(ex.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ErrorDetails> handleTokenValidation(TokenValidationException ex, WebRequest req) {
        ErrorDetails err = new ErrorDetails(ex.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

}
