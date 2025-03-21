<<<<<<< HEAD
<<<<<<< HEAD
//Huỳnh Việt Đan - 22110306
=======
>>>>>>> a9919c7f42d9d5ba1067beae7910aa5256e2e76f
=======
//Huỳnh Việt Đan - 22110306
>>>>>>> dev_dan
package com.example.api.utils;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        FieldError errorValid = ex.getBindingResult().getFieldError();
        assert errorValid != null;
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                errorValid.getDefaultMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
