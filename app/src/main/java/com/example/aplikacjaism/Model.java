package com.example.aplikacjaism;

public class Model<T, K, U> {
    T pizzaName;
    K pizzaDescription;
    U pizzaImage;

    public Model(T pizzaName, K pizzaDescription, U pizzaImage) {
        this.pizzaName = pizzaName;
        this.pizzaDescription = pizzaDescription;
        this.pizzaImage = pizzaImage;
    }
}
