package com.example.aplikacjaism.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT COUNT(*) FROM Pizza")
    int size();

    @Query("SELECT * FROM Pizza WHERE id = :getById")
    Pizza getById(int getById);


    @Insert
    void insert(Pizza pizza);

    @Delete
    void delete(Pizza pizza);

    @Update
    void update(Pizza pizza);
}