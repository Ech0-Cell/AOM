package org.mth3902.aom.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BalanceTransactionRequest {


    @NotNull(message = "customerId is mandatory")
    private Long customerId;

    @Pattern(regexp = "5\\d{9}", message = "Invalid msisdn format")
    @NotBlank(message = "msisdn is mandatory")
    private String msisdn;

    @NotNull(message = "amount is mandatory")
    private Long amount;

    public Long getCustomerId() {
        return customerId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public Long getAmount() {
        return amount;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
