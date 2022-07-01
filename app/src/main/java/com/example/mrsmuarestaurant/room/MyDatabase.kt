package com.example.mrsmuarestaurant.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mrsmuarestaurant.model.Address
import com.example.mrsmuarestaurant.model.Product


@Database(entities = [Product::class, Address::class] /* List model Ex:NoteModel */, version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun daoCart(): DaoCart
    abstract fun daoAddress(): DaoAddress

    companion object {
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase? {
            if (INSTANCE == null) {
                synchronized(MyDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            MyDatabase::class.java, "MyDatabase99902" // Database Name
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}