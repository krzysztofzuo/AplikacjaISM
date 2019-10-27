package com.example.aplikacjaism.pizzapackage;

public class Pizza {
    private String pizzaName;
    private String pizzaDescription;

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getPizzaDescription() {
        return pizzaDescription;
    }

    public void setPizzaDescription(String pizzaDescription) {
        this.pizzaDescription = pizzaDescription;
    }

    public Pizza() {
    }

    public Pizza(String pizzaName, String pizzaDescription, String pizzaImage) {
        this.pizzaName = pizzaName;
        this.pizzaDescription = pizzaDescription;
    }
}