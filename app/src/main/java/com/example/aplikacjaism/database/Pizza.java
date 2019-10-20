package com.example.aplikacjaism.database;

public class Pizza {
    private int id = 0;
    private String pizzaName;
    private String pizzaDescription;
    private String pizzaImage;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getPizzaDescription() {
        return pizzaDescription;
    }

    public void setPizzaDescription(String pizzaDescription) { this.pizzaDescription = pizzaDescription; }

    public String getPizzaImage() { return pizzaImage; }

    public void setPizzaImage(String pizzaImage) {
        this.pizzaImage = pizzaImage;
    }

    public Pizza() {
    }

    public Pizza(int id, String pizzaName, String pizzaDescription, String pizzaImage) {
        this.id = id;
        this.pizzaName = pizzaName;
        this.pizzaDescription = pizzaDescription;
        this.pizzaImage = pizzaImage;
    }
}