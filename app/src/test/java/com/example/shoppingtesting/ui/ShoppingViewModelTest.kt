package com.example.shoppingtesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shoppingtesting.MainCoroutineRule
import com.example.shoppingtesting.Resources.Status
import com.example.shoppingtesting.constants.Constants.MAX_NAME_LENGTH
import com.example.shoppingtesting.constants.Constants.MAX_PRICE_LENGTH
import com.example.shoppingtesting.getOrAwaitValueTest
import com.example.shoppingtesting.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())

    }

    @Test
    fun getShoppingItem() {
    }

    @Test
    fun getTotalPrice() {
    }

    @Test
    fun getImage() {
    }

    @Test
    fun getCurrentImageUrl() {
    }

    @Test
    fun getInsertShoppingItemStatus() {
    }

    @Test
    fun setCurrentImageUrl() {
    }

    @Test
    fun deleteShoppingItem() {
    }

    @Test
    fun insertShoppingItemIntoDb() {
    }

    @Test
    fun `insertShoppingItem with empty field, returns error`() {
        viewModel.insertShoppingItem("name", "", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insertShoppingItem with too long name, returns error`() {
        val string = buildString {
            for (i in 1..MAX_NAME_LENGTH + 1){
                append(1)
            }
        }
        viewModel.insertShoppingItem(string, "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insertShoppingItem with too long price, returns error`() {
        val string = buildString {
            for (i in 1..MAX_PRICE_LENGTH + 1){
                append(1)
            }
        }
        viewModel.insertShoppingItem("name", "5", string)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insertShoppingItem with too high amount, returns error `() {
        viewModel.insertShoppingItem("name", "99999999999999999999999999999999999", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insertShoppingItem with valid amount, returns success `() {
        viewModel.insertShoppingItem("name", "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }


}