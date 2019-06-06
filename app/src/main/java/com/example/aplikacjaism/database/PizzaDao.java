package com.example.aplikacjaism.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PizzaDao {
    @Query("SELECT * FROM Pizza")
    List<Pizza> getAll();

    @Query("SELECT * FROM Pizza WHERE id IN (:userIds)")
    List<Pizza> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Pizza WHERE pizzaName LIKE :first AND " +
            "pizzaDescription LIKE :last LIMIT 1")
    Pizza findByName(String first, String last);

    @Insert
    void insert(Pizza pizza);

    @Delete
    void delete(Pizza pizza);
}