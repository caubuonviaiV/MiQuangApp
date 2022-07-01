package com.example.mrsmuarestaurant.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.mrsmuarestaurant.model.Product

@Dao
interface DaoCart{

    @Insert(onConflict = REPLACE)
    fun insert(data: Product)

    @Delete
    fun delete(data: Product)

    @Delete
    fun delete(data: List<Product>)

    @Update
    fun update(data: Product): Int

    @Query("SELECT * from carts ORDER BY id ASC")
    fun getAll(): List<Product>

    @Query("SELECT * FROM carts WHERE id = :id LIMIT 1")
    fun getProduct(id: Int): Product

    @Query("DELETE FROM carts WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM carts")
    fun deleteAll(): Int
}