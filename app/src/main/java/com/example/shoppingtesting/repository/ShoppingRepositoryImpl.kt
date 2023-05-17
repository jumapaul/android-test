package com.example.shoppingtesting.repository

import androidx.lifecycle.LiveData
import com.example.shoppingtesting.Resources.Resource
import com.example.shoppingtesting.data.local.ShoppingDao
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.PixaApi
import com.example.shoppingtesting.data.remote.PixaData
import java.lang.Exception
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val dao: ShoppingDao,
    private val api: PixaApi
) : ShoppingRepository {

    override suspend fun addItem(shoppingItem: ShoppingItem) {
        dao.addItem(shoppingItem)
    }

    override suspend fun deleteItem(shoppingItem: ShoppingItem) {
        dao.deleteItem(shoppingItem)
    }

    override fun getAllItem(): LiveData<List<ShoppingItem>> {
        return dao.getAllItem()
    }

    override fun getTotalPrice(): LiveData<Float> {
        return dao.getTotalPrice()
    }

    override suspend fun getImage(imageQuery: String): Resource<PixaData> {
        return try {
            val response = api.searchImage(imageQuery)

            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                }?: Resource.error("An unknown error occurred", null)
            }else{
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}