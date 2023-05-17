package com.example.shoppingtesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.shoppingtesting.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
// @RunWith(AndroidJUnit4::class) Android test instrument and they need android components
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {
// Telling jUnit that we want to test all the test cases inside this class one after another
    // since live data is asynchronous
    // We define a rule

    @get:Rule // hilt rule
    var hiltRule = HiltAndroidRule(this)


    @get:Rule // Telling Junit that this is a rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //    private lateinit var database: ShoppingDatabase
    @Inject
    @Named("test_db") // To ensure we are injecting the test.db in our class.
    lateinit var database: ShoppingDatabase
    private lateinit var dao: ShoppingDao

    @Before // We first create the database
    fun setUp() {
        // In memory means the database will be running in our ram
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            ShoppingDatabase::class.java
//        ).allowMainThreadQueries().build()

        //Since hilt will do us the injection, we do not need to create database on our
        // own. Instead
        hiltRule.inject()
        dao = database.getDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addItemTest() = runBlockingTest {
        // Run blocking test is optimized for testing
        val shoppingItem = ShoppingItem("name", 1, 1f, "image", 1)
        dao.addItem(shoppingItem)

        val allShoppingItem = dao.getAllItem().getOrAwaitValue()
        // This returns a live data and not the list
        // Live data runs asynchronous and we don't want that in our test case.
        // For this we use a special function by google LiveDataUtilAndroidTest
        assertThat(allShoppingItem).contains(shoppingItem)

    }

    @Test
    fun deleteItemTest() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "image", 1)
        dao.addItem(shoppingItem)

        dao.deleteItem(shoppingItem)

        val allShoppingItem = dao.getAllItem().getOrAwaitValue()

        assertThat(allShoppingItem).doesNotContain(shoppingItem)
    }

    @Test
    fun getTotalPriceTest() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name", 1, 1f, "image", 1)
        val shoppingItem2 = ShoppingItem("name", 10, 1.1f, "image", 2)
        val shoppingItem3 = ShoppingItem("name", 3, 1.5f, "image", 3)

        dao.addItem(shoppingItem1)
        dao.addItem(shoppingItem2)
        dao.addItem(shoppingItem3)

        val totalPrice = dao.getTotalPrice().getOrAwaitValue()

        assertThat(totalPrice).isEqualTo(1 * 1f + 10 * 1.1f + 3 * 1.5f)

    }


}