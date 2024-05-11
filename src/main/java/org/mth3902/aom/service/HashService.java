package org.mth3902.aom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashService {

    private final BCryptPasswordEncoder passwordEncoder;

    public HashService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Method to hash the password
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Method to check if the provided password matches the hashed password
    public boolean checkPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
