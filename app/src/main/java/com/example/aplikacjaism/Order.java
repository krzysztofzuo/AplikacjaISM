package com.example.aplikacjaism;

import com.example.aplikacjaism.pizzapackage.Pizza;

import java.util.Date;

public class Order {
    private Pizza pizza;
    private Date date;

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


    public Order() {
    }

    public Order(Pizza pizza, Date date) {
        this.pizza = pizza;
        this.date = date;
    }
}
