package org.mth3902.aom.controller;

import jakarta.validation.Valid;
import org.mth3902.aom.DTO.BalanceTransactionRequest;
import org.mth3902.aom.model.Balance;
import org.mth3902.aom.model.Package;
import org.mth3902.aom.repository.BalanceRepository;
import org.mth3902.aom.repository.PackageRepository;
import org.mth3902.aom.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final AuthenticationService auth;
    private final BalanceRepository balanceRepository;
    private final PackageRepository packageRepository;

    @Autowired
    public BalanceController(AuthenticationService auth,
                             BalanceRepository balanceRepository,
                             PackageRepository packageRepository) {

        this.packageRepository = packageRepository;
        this.balanceRepository = balanceRepository;
        this.auth = auth;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getBalanceByMSISDN(@RequestParam String MSISDN, @RequestHeader("Authorization") String token) throws Exception {

        Map<String, Object> responseBody = new HashMap<String, Object>();

        if(!auth.isValidToken(token, MSISDN)) {
            responseBody.put("message", "invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }

        Balance balance = balanceRepository.getBalanceByMSISDN(MSISDN);
        Package cellPackage = packageRepository.getPackageById(balance.getPackageId());
        responseBody.put("balance", balance);
        responseBody.put("package", cellPackage);

        return ResponseEntity.ok(responseBody);
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "validation error");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
