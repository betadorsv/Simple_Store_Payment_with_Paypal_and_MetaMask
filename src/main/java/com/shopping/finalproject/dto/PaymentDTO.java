package com.shopping.finalproject.dto;

import lombok.Data;

import javax.persistence.Entity;

@Data
public class PaymentDTO {
    private String cardHolderName;
    private String cvvNumber;
    private String cardNumber;
    private String address;
    private String description;
}
