package org.mth3902.aom.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Customer {

    @NotBlank(message = "msisdn is mandatory")
    @Pattern(regexp = "5\\d{9}", message = "Invalid msisdn format")
    private String msisdn;
    @NotBlank(message = "name is mandatory")
    private String name;
    @NotBlank(message = "surname is mandatory")
    private String surname;
    @NotBlank(message = "email is mandatory")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "password is mandatory")
    private String password;
    private Long packageID;
    private String securityKey;

    public Customer(String msisdn,
                    Long packageID,
                    String name,
                    String surname,
                    String email,
                    String password,
                    String securityKey) {
        this.msisdn = msisdn;
        this.packageID = packageID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.securityKey = securityKey;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public Long getPackageID() {
        return packageID;
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

    public String getPassword() {
        return password;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setPackageID(Long packageID) {
        this.packageID = packageID;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "msisdn='" + msisdn + '\'' +
                ", packageID=" + packageID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", securityKey='" + securityKey + '\'' +
                '}';
    }
}
