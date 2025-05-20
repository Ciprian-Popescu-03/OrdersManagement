package com.ordersmanagement.Model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int clientId;

    public Order() {

    }

    public Order(int id, int clientId, LocalDateTime orderDate) {
        this.id = id;
        this.clientId = clientId;
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }


    public void setClientId(int id) {
        this.clientId = id;
    }
}
