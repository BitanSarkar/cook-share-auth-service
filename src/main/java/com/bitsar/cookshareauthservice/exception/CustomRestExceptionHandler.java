package com.bitsar.cookshareauthservice.exception;

import com.bitsar.cookshareauthservice.dto.ResponseWrap;
import com.bitsar.cookshareauthservice.util.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String PACKAGE_NAME = "com.bitsar.cookshareauthservice";


    /**
     * Handles all exceptions and returns a failure response with error message.
     *
     * @param ex      the exception to handle
     * @param request the web request
     * @return the response entity with failure response
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseWrap<Object>> handleAll(final Exception ex, final WebRequest request) {
        logError(ex);
        return ResponseBuilder.getFailureResponse(ex.getMessage(), 0);
    }

    /**
     * Handle all runtime exceptions and return a failure response.
     *
     * @param ex      The exception that was thrown
     * @param request The web request
     * @return A response entity wrapping the failure response
     */
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResponseWrap<Object>> handleAllBadRequest(final Exception ex, final WebRequest request) {
        logError(ex);
        return ResponseBuilder.getFailureResponse(ex.getMessage(), 1);
    }

    /**
     * Handle invalid phone number format exception
     *
     * @param ex      the exception to handle
     * @param request the web request
     * @return the response wrap
     */
    @ExceptionHandler({InvalidPhoneNumberFormatException.class})
    public ResponseEntity<ResponseWrap<Object>> handleAllBadRequest(final InvalidPhoneNumberFormatException ex, final WebRequest request) {
        logError(ex);
        return ResponseBuilder.getFailureResponse(ex.getMessage(), 2);
    }

    /**
     * Exception handler for PhoneNumberAlreadyExistsException
     *
     * @param ex      The exception to be handled
     * @param request The web request
     * @return ResponseEntity containing the failure response
     */
    @ExceptionHandler({PhoneNumberAlreadyExistsException.class})
    public ResponseEntity<ResponseWrap<Object>> handleAllBadRequest(final PhoneNumberAlreadyExistsException ex, final WebRequest request) {
        logError(ex);
        return ResponseBuilder.getFailureResponse(ex.getMessage(), 3);
    }

    /**
     * Handle SecretHashGenerationException and return a failure response
     *
     * @param ex      The SecretHashGenerationException to handle
     * @param request The current web request
     * @return A ResponseEntity containing a failure response
     */
    @ExceptionHandler({SecretHashGenerationException.class})
    public ResponseEntity<ResponseWrap<Object>> handleAllBadRequest(final SecretHashGenerationException ex, final WebRequest request) {
        logError(ex);
        return ResponseBuilder.getFailureResponse(ex.getMessage(), 4);
    }

    /**
     * Handles InvalidRegistrationSessionException and returns a failure response.
     *
     * @param ex      the InvalidRegistrationSessionException
     * @param request the WebRequest
     * @return a ResponseEntity containing a failure response
     */
    @ExceptionHandler({InvalidRegistrationSessionException.class})
    public ResponseEntity<ResponseWrap<Object>> handleAllBadRequest(final InvalidRegistrationSessionException ex, final WebRequest request) {
        logError(ex);
        return ResponseBuilder.getFailureResponse(ex.getMessage(), 5);
    }

    /**
     * Handles IncorrectOTPException and returns a failure response.
     *
     * @param ex      the IncorrectOTPException
     * @param request the WebRequest
     * @return a ResponseEntity containing a failure response
     */
    @ExceptionHandler({IncorrectOTPException.class})
    public ResponseEntity<ResponseWrap<Object>> handleAllBadRequest(final IncorrectOTPException ex, final WebRequest request) {
        logError(ex);
        return ResponseBuilder.getFailureResponse(ex.getMessage(), 6);
    }

    /**
     * Handle NotAuthorizedToLoginException and return a failure response
     *
     * @param ex      the NotAuthorizedToLoginException
     * @param request the web request
     * @return a ResponseEntity with a failure response
     */
    @ExceptionHandler({NotAuthorizedToLoginException.class})
    public ResponseEntity<ResponseWrap<Object>> handleAllBadRequest(final NotAuthorizedToLoginException ex, final WebRequest request) {
        logError(ex);
        return ResponseBuilder.getFailureResponse(ex.getMessage(), 7);
    }

    /**
     * Exception handler for InvalidSessionException
     *
     * @param ex       the InvalidSessionException
     * @param request  the WebRequest
     * @return         a ResponseEntity with a failure response
     */
    @ExceptionHandler({InvalidSessionException.class})
    public ResponseEntity<ResponseWrap<Object>> handleAllBadRequest(final InvalidSessionException ex, final WebRequest request) {
        logError(ex);
        return ResponseBuilder.getFailureResponse(ex.getMessage(), 8);
    }

    /**
     * Logs the error and its location in the code.
     *
     * @param ex the exception to be logged
     */
    private void logError(Exception ex) {
        // Find the first stack trace element that matches the package name and is not a lambda expression
        Optional<StackTraceElement> logTrace = Arrays.stream(ex.getStackTrace())
                .filter(errorLog -> errorLog.getClassName().startsWith(PACKAGE_NAME) && !errorLog.getClassName().contains("$$"))
                .findFirst();

        // If a matching stack trace element is found, log the error location
        if (logTrace.isPresent()) {
            StackTraceElement ste = logTrace.get();
            log.info("Error in class {} in {}() : {}", ste.getClassName(), ste.getMethodName(), ex.toString());
        }

        // Log the error
        log.error("Error", ex);
    }

}
