package com.example.aplikacjaism;

import com.example.aplikacjaism.pizzapackage.Pizza;
import com.example.aplikacjaism.trackingpackage.Order;
import com.example.aplikacjaism.userpackage.User;

import java.util.List;

public interface DataStatus {
    void DataIsLoaded(List<Pizza> pizzas, List<String> keys);

    void DataIsInserted();

    void DataIsUpdated();

    void DataIsDeleted();

    void DataUsersIsLoaded(List<User> users, List<String> keys);

    void DataOrdersIsLoaded(List<Order> orders, List<String> keys);
}
