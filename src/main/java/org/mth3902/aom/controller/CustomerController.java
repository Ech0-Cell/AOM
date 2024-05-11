package org.mth3902.aom.controller;

import jakarta.validation.Valid;
import org.mth3902.aom.model.Customer;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/customer")
public class CustomerController {


    private final AuthenticationService auth;
    private final HashService hash;

    private VoltDatabase voltDB;
    private Environment env;

    @Autowired
    public CustomerController(AuthenticationService auth, HashService hash, Environment env) {
        this.auth = auth;
        this.hash = hash;
        this.env = env;
        try {
            this.voltDB = new VoltDatabase(env.getProperty("voltdb.server.host"));
        } catch (Exception e) {
            System.out.println("error in voltdb" + e);
        }

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/register",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<String> register(@Valid @RequestBody Customer body) {

        System.out.println(body);
        String hashedPassword = hash.hashPassword(body.getPassword());

        try {
            voltDB.insertCustomer(2,
                    body.getMsisdn(),
                    body.getName(),
                    body.getSurname(),
                    body.getEmail(),
                    hashedPassword,
                    body.getPackageID() + "",
                    body.getSecurityKey());

        } catch (Exception e) {

            System.out.println("failed to insert at voltdb:  " + e);
            return ResponseEntity.internalServerError().body("failed to insert at voltdb");

        }

        //TODO insert to oracledb
        //TODO insert to hazelcast

        return new ResponseEntity<String>("success",HttpStatus.CREATED);
    }

    @PostMapping(path = "/login",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {

        Map<String, String> responseBody = new HashMap<String, String>();

        //TODO This should come from voltDB
        String hashedPasswordDB = hash.hashPassword(body.get("password"));;
        String msisdnDB = body.get("msisdn");

        if(body.get("msisdn").equals(msisdnDB)) {
            if(hash.checkPassword(body.get("password"), hashedPasswordDB)) {
                String token = auth.generateToken(body.get("msisdn"));

                responseBody.put("token", token);
                responseBody.put("msisdn", body.get("msisdn"));

                return ResponseEntity.ok(responseBody);
            }
        }

        responseBody.put("error", "Wrong msisdn or password.");
        return ResponseEntity.badRequest().body(responseBody);
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
