package com.sample.api.exceptions;

import com.sample.exceptions.AlreadyExistsException;
import com.sample.exceptions.BadRequestException;
import com.sample.exceptions.NotFoundException;
import com.sample.models.Model;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.List;

import static com.sample.api.exceptions.ApiExceptionHandler.LoggerLevel.DEBUG;
import static com.sample.api.exceptions.ApiExceptionHandler.LoggerLevel.NONE;
import static com.sample.api.exceptions.ApiExceptionHandler.LoggerLevel.TRACE;
import static com.sample.api.exceptions.ApiExceptionHandler.LoggerLevel.WARN;

@ControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * Spring exceptions were taken from
     *
     * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
     */
    @ExceptionHandler({
            AlreadyExistsException.class,
            NotFoundException.class,
            BadRequestException.class,

            MethodArgumentNotValidException.class,

            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class
    })
    public ResponseEntity<Object> handleServiceExceptions(final Exception exception) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("### Processing {} ###", exception.getClass().getName());
        }
        final ErrorAttributes error = new ErrorAttributes(exception);
        return ResponseEntity.status(error.status).body(error);
    }

    enum LoggerLevel {
        NONE {
            @Override
            void logError(final String message, final Exception ex) {
                // nothing to do
            }
        },
        TRACE {
            @Override
            void logError(final String message, final Exception ex) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(message, ex);
                }
            }
        },
        DEBUG {
            @Override
            void logError(final String message, final Exception ex) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(message, ex);
                }
            }
        },
        INFO {
            @Override
            void logError(final String message, final Exception ex) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(message, ex);
                }
            }
        },
        WARN {
            @Override
            void logError(final String message, final Exception ex) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(message, ex);
                }
            }
        },
        ERROR {
            @Override
            void logError(final String message, final Exception ex) {
                LOGGER.error(message, ex);
            }
        };

        abstract void logError(final String message, final Exception ex);
    }

    enum SupportedException {
        ALREADY_EXISTS(AlreadyExistsException.class, HttpStatus.CONFLICT, DEBUG),
        NOT_FOUND(NotFoundException.class, HttpStatus.NOT_FOUND, DEBUG),
        BAD_REQUEST(BadRequestException.class, HttpStatus.BAD_REQUEST, DEBUG),
        VALIDATION_ERROR(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST, "Validation Error", DEBUG),

        SPRING_1(HttpRequestMethodNotSupportedException.class, HttpStatus.BAD_REQUEST, "Request method not supported", TRACE),
        SPRING_2(HttpMediaTypeNotSupportedException.class, HttpStatus.BAD_REQUEST, "Content type not supported", TRACE),
        SPRING_3(HttpMediaTypeNotAcceptableException.class, HttpStatus.BAD_REQUEST, "Media type not supported", TRACE),
        SPRING_4(MissingPathVariableException.class, HttpStatus.BAD_REQUEST, "Missing URI template variable", TRACE),
        SPRING_5(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST, "Required parameter is not present", TRACE),
        SPRING_6(ServletRequestBindingException.class, HttpStatus.INTERNAL_SERVER_ERROR, TRACE),
        SPRING_7(ConversionNotSupportedException.class, HttpStatus.INTERNAL_SERVER_ERROR, TRACE),
        SPRING_8(TypeMismatchException.class, HttpStatus.BAD_REQUEST, "Type mismatch", TRACE),
        SPRING_9(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST, "Required request body is missing", TRACE),
        SPRING_10(HttpMessageNotWritableException.class, HttpStatus.INTERNAL_SERVER_ERROR, TRACE),
        SPRING_11(MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST, "Required request part is not present", TRACE),
        SPRING_12(BindException.class, HttpStatus.INTERNAL_SERVER_ERROR, TRACE),
        SPRING_13(NoHandlerFoundException.class, HttpStatus.INTERNAL_SERVER_ERROR, TRACE),
        SPRING_14(AsyncRequestTimeoutException.class, HttpStatus.SERVICE_UNAVAILABLE, TRACE),

        UNKNOWN(Exception.class, HttpStatus.INTERNAL_SERVER_ERROR, "Some unknown error happened. Please try again later", WARN);

        final Class exceptionClass;

        final HttpStatus status;

        final String message;

        final LoggerLevel loggerLevel;

        <T extends Exception> SupportedException(final Class<T> exceptionClass, final HttpStatus status, final String message,
                                                 final LoggerLevel loggerLevel) {
            this.exceptionClass = exceptionClass;
            this.status = status;
            this.message = message;
            this.loggerLevel = loggerLevel;
        }

        <T extends Exception> SupportedException(final Class<T> exceptionClass, final HttpStatus status, final String message) {
            this(exceptionClass, status, message, NONE);
        }

        <T extends Exception> SupportedException(final Class<T> exceptionClass, final HttpStatus status, final LoggerLevel loggerLevel) {
            this(exceptionClass, status, null, loggerLevel);
        }

        <T extends Exception> SupportedException(final Class<T> exceptionClass, final HttpStatus status) {
            this(exceptionClass, status, null, NONE);
        }

        static <T extends Exception> SupportedException get(final Class<T> exceptionClass) {
            for (final SupportedException supportedException : SupportedException.values()) {
                if (supportedException.exceptionClass.equals(exceptionClass)) {
                    return supportedException;
                }
            }
            return UNKNOWN;
        }
    }

    @Getter
    static class ErrorAttributes extends Model {

        private final Date timestamp;

        private final int status;

        private final String error;

        private final String message;

        private final List<?> errors;

        ErrorAttributes(final Exception exception) {
            List<?> customErrors = null;
            if (exception instanceof MethodArgumentNotValidException) {
                customErrors = ApiValidationErrorHandler.handle((MethodArgumentNotValidException) exception);
            }

            final SupportedException supportedException = SupportedException.get(exception.getClass());
            if (SupportedException.UNKNOWN.equals(supportedException)) {
                LOGGER.warn("Unknown error", exception);
            }
            timestamp = new Date();
            status = supportedException.status.value();
            error = supportedException.status.getReasonPhrase();
            message = supportedException.message;
            errors = customErrors;
        }
    }
}
