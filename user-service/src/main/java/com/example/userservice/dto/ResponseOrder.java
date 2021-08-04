package com.example.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class ResponseOrder {
    private String productId;
    private String qty;
    private String unitPrice;
    private String totalPrice;
    private String createdAt;
    private String orderId;

}

