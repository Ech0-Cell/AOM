package org.mth3902.aom.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterCustomerRequest {
    @NotBlank(message = "msisdn is mandatory")
    @Pattern(regexp = "5\\d{9}", message = "Invalid msisdn format")
    private final String msisdn;
    @NotBlank(message = "name is mandatory")
    private final String name;
    @NotBlank(message = "surname is mandatory")
    private final String surname;
    @NotBlank(message = "email is mandatory")
    @Email(message = "Invalid email format")
    private final String email;
    @NotBlank(message = "password is mandatory")
    private final String password;
    private final Long packageID;
    private final String securityKey;

    public RegisterCustomerRequest(String msisdn,
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
