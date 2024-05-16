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
import org.voltdb.VoltTableRow;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = "api/customer")
public class CustomerController {


    private final AuthenticationService auth;
    private final HashService hash;
    private VoltDatabase voltDB;

    @Autowired
    public CustomerController(AuthenticationService auth, HashService hash, Environment env) {
        this.auth = auth;
        this.hash = hash;
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
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody Customer body) {

        Map<String, Object> responseBody = new HashMap<String, Object>();
        String hashedPassword = hash.hashPassword(body.getPassword());

        //TODO package_id is missing in voltDB
        // status in unknown

        try {
            if(voltDB.selectCustomerByMSISDN(body.getMsisdn()).advanceRow()) { //check if customer exist
                responseBody.put("message", "Customer with same MSISDN already exists");
                return ResponseEntity.badRequest().body(responseBody);
            }


            int nextId = voltDB.getNextCustomerId();
            System.out.println(nextId);

            voltDB.insertCustomer(nextId,
                    body.getMsisdn(),
                    body.getName(),
                    body.getSurname(),
                    body.getEmail(),
                    hashedPassword,
                    "1",
                    body.getSecurityKey());

        } catch (Exception e) {

            System.out.println("failed to insert at voltdb:  " + e);
            responseBody.put("message", "failed to insert at voltdb");
            return ResponseEntity.internalServerError().body(responseBody);
        }

        //TODO insert to oracledb
        //TODO insert to hazelcast
        responseBody.put("message", "successfully registered");
        responseBody.put("customer", body);
        return new ResponseEntity<Map<String, Object>>(responseBody,HttpStatus.CREATED);
    }

    @PostMapping(path = "/login",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {

        Map<String, String> responseBody = new HashMap<String, String>();

        try {
            VoltTable table = voltDB.selectCustomerByMSISDN(body.get("msisdn"));

            if(table.advanceRow()) //check if there is customer with such msisdn
            {
                VoltTableRow row = table.fetchRow(0);
                String password = row.getString("password");
                String msisdn = row.getString("msisdn");

                if(hash.checkPassword(body.get("password"), password))  //check if password is correct
                {
                    String token = auth.generateToken(body.get("msisdn"));

                    responseBody.put("token", token);
                    responseBody.put("msisdn", body.get("msisdn"));

                    return ResponseEntity.ok(responseBody);
                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
            responseBody.put("message", "voltdb error");
            return ResponseEntity.internalServerError().body(responseBody);
        }

        responseBody.put("message", "Wrong msisdn or password");
        return ResponseEntity.badRequest().body(responseBody);
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
