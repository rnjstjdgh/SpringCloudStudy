package com.example.orderservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
