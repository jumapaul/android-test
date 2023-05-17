package com.example.shoppingtesting.repository

import androidx.lifecycle.LiveData
import com.example.shoppingtesting.Resources.Resource
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.PixaData

interface ShoppingRepository {

    suspend fun addItem(shoppingItem: ShoppingItem)

    suspend fun deleteItem(shoppingItem: ShoppingItem)

    fun getAllItem(): LiveData<List<ShoppingItem>>

    fun getTotalPrice(): LiveData<Float>

    suspend fun getImage(imageQuery: String): Resource<PixaData>
}