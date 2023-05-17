package com.example.shoppingtesting.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItem::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase: RoomDatabase() {

    abstract fun getDao(): ShoppingDao
}