package org.mth3902.aom.controller;

import jakarta.validation.Valid;
import org.mth3902.aom.DTO.BalanceTransactionRequest;
import org.mth3902.aom.model.Balance;
import org.mth3902.aom.service.AuthenticationService;
import org.mth3902.aom.service.HashService;
import org.mth3902.aom.voltdb.VoltDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/balance")
public class BalanceController {

    private VoltDatabase voltDB;
    private AuthenticationService auth;

    @Autowired
    public BalanceController(Environment env, AuthenticationService auth) {
        this.auth = auth;
        try {
            this.voltDB = new VoltDatabase(env.getProperty("voltdb.server.host"));
        } catch (Exception e) {
            System.out.println("error in voltdb" + e);
        }
    }

    @GetMapping
    public ResponseEntity getBalanceByMSISDN(@RequestParam String MSISDN, @RequestHeader("Authorization") String token) {

        if(!auth.isValidToken(token, MSISDN))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");

        try {
            VoltTable table = voltDB.selectBalanceByMSISDN(MSISDN);

            if(table.advanceRow())
            {
                VoltTableRow row = table.fetchRow(0);

                Balance balance = new Balance(
                        row.get("BAL_LVL_DATA", VoltType.INTEGER).toString(),
                        row.get("BAL_LVL_SMS", VoltType.INTEGER).toString(),
                        row.get("BAL_LVL_MINUTES", VoltType.INTEGER).toString(),
                        row.getDecimalAsBigDecimal("BAL_LVL_MONEY"),
                        row.getTimestampAsLong("EDATE"),
                        row.getTimestampAsLong("SDATE"),
                        row.getDecimalAsBigDecimal("PRICE")
                        );

                return ResponseEntity.ok(balance);
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body("failed to select from voltdb");
        }

        return ResponseEntity.badRequest().body("no balance is associated with this customer");
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
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
