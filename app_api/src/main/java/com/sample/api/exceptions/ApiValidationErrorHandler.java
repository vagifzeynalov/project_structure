package com.sample.api.exceptions;

import com.sample.models.Model;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sample.api.exceptions.ApiValidationErrorHandler.ValidationErrorCodes.UNKNOWN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ApiValidationErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiValidationErrorHandler.class);

    private static final Map<String, ValidationErrorCodes> ALL_CODES = new HashMap<>();

    static {
        for (final ValidationErrorCodes value : ValidationErrorCodes.values()) {
            ALL_CODES.put(value.codeString, value);
        }
    }

    static List<ApiValidationError> handle(final MethodArgumentNotValidException exception) {
        if (exception.getBindingResult().getAllErrors().isEmpty()) {
            return null;
        }

        final List<ApiValidationError> validationErrors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            if (objectError instanceof FieldError) {
                final FieldError fieldError = (FieldError) objectError;
                final String fieldName = fieldError.getField();
                final String errorMessage = fieldError.getDefaultMessage();
                validationErrors.add(new ApiValidationError(fieldName, errorMessage, getErrorCode(fieldError.getCodes())));
            }
        });
        return validationErrors;
    }

    private static int getErrorCode(final String[] codeStrings) {
        ValidationErrorCodes result;
        if (codeStrings == null || codeStrings.length == 0) {
            LOGGER.warn("The list of codes is empty");
            result = UNKNOWN;
        } else {
            result = ALL_CODES.get(
                    Arrays.stream(codeStrings).filter(ALL_CODES::containsKey).findAny().orElse(UNKNOWN.codeString)
            );
        }
        if (result == UNKNOWN && LOGGER.isWarnEnabled()) {
            LOGGER.warn("Unknown validator codes {}", Arrays.toString(codeStrings));
        }
        return result.code;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    enum ValidationErrorCodes {

        // AddressViewModel fields

        ADDRESS_ID_NULL("Null.addressId", 1100),

        FIRST_NAME_NOT_BLANK("NotBlank.firstName", 1200),
        FIRST_NAME_NOT_EMPTY("NotEmptyString.firstName", 1201),

        LAST_NAME_NOT_BLANK("NotBlank.lastName", 1300),
        LAST_NAME_NOT_EMPTY("NotEmptyString.lastName", 1301),

        STREET_NOT_BLANK("NotBlank.street", 1400),
        STREET_NOT_EMPTY("NotEmptyString.street", 1401),

        CITY_NOT_BLANK("NotBlank.city", 1500),
        CITY_NOT_EMPTY("NotEmptyString.city", 1501),

        ZIP_NOT_BLANK("NotBlank.zip", 1600),
        ZIP_NOT_EMPTY("NotEmptyString.zip", 1601),

        UNKNOWN("UNKNOWN", -1);

        final String codeString;

        final int code;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    static class ApiValidationError extends Model {

        private final String field;

        private final String message;

        private final Integer code;
    }
}
