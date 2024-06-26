package org.mth3902.aom.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Customer {

    private long id;
    private String msisdn;
    private String name;
    private String surname;
    private String email;
    private String hashedPassword;
    private String securityKey;

    public Customer(long id,
                    String msisdn,
                    String name,
                    String surname,
                    String email,
                    String hashedPassword,
                    String securityKey) {
        this.msisdn = msisdn;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.securityKey = securityKey;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashedPassword(String password) {
        this.hashedPassword = password;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "msisdn='" + msisdn + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + hashedPassword + '\'' +
                ", securityKey='" + securityKey + '\'' +
                '}';
    }
}
