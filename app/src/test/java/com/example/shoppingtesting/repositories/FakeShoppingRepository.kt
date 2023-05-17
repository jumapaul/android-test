package com.example.shoppingtesting.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppingtesting.Resources.Resource
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.PixaData
import com.example.shoppingtesting.repository.ShoppingRepository


//This class helps us test the view model

class FakeShoppingRepository : ShoppingRepository {

    // Since this is a testing repository, we don't need a real database.
    // For this, we will create a list to act as our database

    private val shoppingItems = mutableListOf<ShoppingItem>() // Acts as our db

    // The above shopping item acts as our room db only that it isn't persistant
    private val observableShoppingItem =
        MutableLiveData<List<ShoppingItem>>(shoppingItems) // This is used to simulate the behaviour of our live data getAllItems
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false // Used to simulate our response.

    // We then create a function for this
    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    // We should post the value after adding a new item to the list.
    // For this, we add a new function to refresh our list
    private fun refreshLiveData() {
        observableShoppingItem.postValue(shoppingItems)
        observableTotalPrice.postValue(getNewTotalPrice())
    }

    // This returns the new total price after adding the new item

    private fun getNewTotalPrice(): Float {
        // This get the sum of the total price of our items
        return shoppingItems.sumByDouble { it.price.toDouble() }.toFloat()
    }

    override suspend fun addItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun getAllItem(): LiveData<List<ShoppingItem>> {
        return observableShoppingItem
    }

    override fun getTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun getImage(imageQuery: String): Resource<PixaData> {
        return if (shouldReturnNetworkError){
            Resource.error("Error", null)
        }else{
            Resource.success(PixaData(listOf(), 0, 0))
        }
    }
}