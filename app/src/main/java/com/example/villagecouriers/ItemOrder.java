package com.example.villagecouriers;

public class ItemOrder {

    private String item_name;
    private String item_quantity;
    private String item_price;

    public ItemOrder(String item_name, String item_quantity, String item_price) {
        this.item_name = item_name;
        this.item_quantity = item_quantity;
        this.item_price = item_price;

    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }


}
