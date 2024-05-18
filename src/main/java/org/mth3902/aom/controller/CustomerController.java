package org.mth3902.aom.controller;

import jakarta.validation.Valid;
import org.mth3902.aom.model.Customer;
import org.mth3902.aom.repository.CustomerRepository;
import org.mth3902.aom.service.AuthenticationService;
import org.mth3902.aom.service.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/customer")
public class CustomerController {

    private final AuthenticationService auth;
    private final HashService hash;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(AuthenticationService auth,
                              HashService hash,
                              CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
        this.auth = auth;
        this.hash = hash;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/register",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> registerCustomer(@Valid @RequestBody Customer body) throws Exception {

        Map<String, Object> responseBody = new HashMap<String, Object>();
        String hashedPassword = hash.hashPassword(body.getPassword());


        if(!customerRepository.existsByMSISDN(body.getMsisdn())) {

            Customer customer = customerRepository.save(body, hashedPassword);

            responseBody.put("message", "successfully registered");
            responseBody.put("msisdn", customer.getMsisdn());
            return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.CREATED);
        }
        else {
            responseBody.put("message", "Customer with same MSISDN already exists");
            return ResponseEntity.badRequest().body(responseBody);
        }

        //TODO insert to oracledb
        //TODO insert to hazelcast

    }

    @PostMapping(path = "/login",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<Map<String, String>> loginCustomer(@RequestBody Map<String, String> body) throws Exception {

        Map<String, String> responseBody = new HashMap<String, String>();

        Customer customer = customerRepository.getCustomerByMSISDN(body.get("msisdn"));

        if(hash.checkPassword(body.get("password"), customer.getPassword()))  //check if password is correct
        {
            String token = auth.generateToken(customer.getMsisdn());

            responseBody.put("token", token);
            responseBody.put("msisdn", customer.getMsisdn());

            return ResponseEntity.ok(responseBody);
        }

        responseBody.put("message", "Wrong msisdn or password");
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

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
