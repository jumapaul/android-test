package com.example.shoppingtesting.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShoppingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteItem(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM shopping_item")
    fun getAllItem(): LiveData<List<ShoppingItem>>

    @Query("SELECT SUM(price*amount) FROM shopping_item")
    fun getTotalPrice(): LiveData<Float>
}