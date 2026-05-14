package com.hzqaq.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order implements Serializable {

    private String orderId;
    private String productName;
    private BigDecimal amount;
    private Integer quantity;
    private LocalDateTime createTime;

    public Order() {
    }

    public Order(String orderId, String productName, BigDecimal amount, Integer quantity) {
        this.orderId = orderId;
        this.productName = productName;
        this.amount = amount;
        this.quantity = quantity;
        this.createTime = LocalDateTime.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", quantity=" + quantity +
                ", createTime=" + createTime +
                '}';
    }
}
