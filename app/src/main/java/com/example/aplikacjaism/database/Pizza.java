package com.example.aplikacjaism.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Pizza {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "pizzaName")
    private String pizzaName;

    @ColumnInfo(name = "pizzaDescription")
    private String pizzaDescription;

    @ColumnInfo(name = "pizzaImage")
    private String pizzaImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getPizzaImage() {
        return pizzaImage;
    }

    public void setPizzaImage(String pizzaImage) {
        this.pizzaImage = pizzaImage;
    }
}