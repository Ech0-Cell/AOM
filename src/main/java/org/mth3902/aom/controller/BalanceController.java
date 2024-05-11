package org.mth3902.aom.controller;

import jakarta.validation.Valid;
import org.mth3902.aom.DTO.BalanceTransactionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/balance")
public class BalanceController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<String> getBalanceByMSISDN(@RequestParam String MSISDN) {
        System.out.println(MSISDN);

        return ResponseEntity.ok(MSISDN);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(path = "/update/data")
    public ResponseEntity<String> updateData(@Valid @RequestBody BalanceTransactionRequest body) {
        System.out.println(body);
        return ResponseEntity.ok("updateData");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(path = "/update/minute")
    public ResponseEntity<String> updateMinute(@Valid @RequestBody BalanceTransactionRequest body) {
        System.out.println(body);
        return ResponseEntity.ok("updateMinute");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(path = "/update/sms")
    public ResponseEntity<String> updateSMS(@Valid @RequestBody BalanceTransactionRequest body) {
        System.out.println(body);
        return ResponseEntity.ok("updateSMS");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(path = "/update/money")
    public ResponseEntity<String> updateMoney(@Valid @RequestBody BalanceTransactionRequest body) {
        System.out.println(body);
        return ResponseEntity.ok("updateMoney");
    }

    //TODO extract validation handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
