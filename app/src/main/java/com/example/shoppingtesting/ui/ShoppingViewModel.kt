package com.example.shoppingtesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingtesting.Resources.Event
import com.example.shoppingtesting.Resources.Resource
import com.example.shoppingtesting.constants.Constants.MAX_NAME_LENGTH
import com.example.shoppingtesting.constants.Constants.MAX_PRICE_LENGTH
import com.example.shoppingtesting.data.local.ShoppingItem
import com.example.shoppingtesting.data.remote.PixaData
import com.example.shoppingtesting.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItem = repository.getAllItem()

    val totalPrice = repository.getTotalPrice()

    // Since the response of our searches will be a list of items,
    // we can create a list to have them.
    private val _images =
        MutableLiveData<Event<Resource<PixaData>>>()// Making actual mutable datas in your view model private

    // so that you can only change them in the view model.
    // We then create a copy of that which is immutable iso that we can access it in our app
    val image: LiveData<Event<Resource<PixaData>>> = _images

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.addItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amount: String, priceString: String) {
        if (name.isEmpty() || amount.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The fields must not be empty",
                        null
                    )
                )
            )
            return
        }

        if (name.length > MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The name of the item" +
                                "must not exceed $MAX_NAME_LENGTH characters", null
                    )
                )
            )

            return
        }

        if (priceString.length > MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The price string of the item" +
                                "must not exceed $MAX_PRICE_LENGTH characters", null
                    )
                )
            )
            return
        }

        val amount = try {
            amount.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Please enter a valid amount",
                        null
                    )
                )
            )
            return
        }
        val shoppingItem = ShoppingItem(name, amount, priceString.toFloat(), _currentImageUrl.value?: "", 0)

        insertShoppingItemIntoDb(shoppingItem) // This method is a coroutine called in viewmodel scope
        // This should be called in a dispatchers.main which is not available in test.
        // This mean we create a rule in our test, maincoroutine rule
        // If this could be in our android test, then it could be easier.
        setCurrentImageUrl("")

        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String){
        if (imageQuery.isEmpty()){
            return
        }

        _images.value = Event(Resource.loading(null))

        viewModelScope.launch {
            val response = repository.getImage(imageQuery)
            _images.value = Event(response)
        }
    }


}