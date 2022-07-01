package com.example.mrsmuarestaurant.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "carts")
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int idTb;

    public int id;
    public String name;
    public String description;
    public String content;
    public int cat_id;
    public String thumb;
    public String price;
    public String price_sale;

    public int total_number;
    public int  total_rating;

    public String created_at;
    public String updated_at;


    public int total = 1;
    public boolean selected = true;
}
