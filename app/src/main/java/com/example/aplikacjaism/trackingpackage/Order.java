package com.example.aplikacjaism.trackingpackage;

import com.example.aplikacjaism.pizzapackage.Pizza;

import java.util.Date;

public class Order {
    private Pizza pizza;
    private Date date;
    private com.example.aplikacjaism.trackingpackage.LatLng coordinates;
    private String userId;

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public com.example.aplikacjaism.trackingpackage.LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(com.example.aplikacjaism.trackingpackage.LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Order() {
    }

    public Order(Pizza pizza, Date date, LatLng coordinates, String userId) {
        this.pizza = pizza;
        this.date = date;
        this.coordinates = coordinates;
        this.userId = userId;
    }
}
