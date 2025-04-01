package com.example.villagecouriers;

public class Order {
    private long orderId;
    private String itemName;
    private long userId;
    private int quantity;
    private double price;

    public Order(long orderId, String itemName,int quantity, double price) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and setters
    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getItemName() {
        return itemName;
    }
}
