package com.example.fascinations.core;

import java.io.Serializable;
import java.util.Map;

public class Bill implements Serializable {
    private int transactionId;
    private Map<String, Integer> orderMap;
    private int amount;

    public Bill(int transactionId, Map<String, Integer> orderMap, int amount) {
        this.transactionId = transactionId;
        this.orderMap = orderMap;
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Map<String, Integer> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<String, Integer> orderMap) {
        this.orderMap = orderMap;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
