package com.example.shoppingtesting.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppingtesting.constants.Constants.BASE_URL
import com.example.shoppingtesting.constants.Constants.DATABASE_NAME
import com.example.shoppingtesting.data.local.ShoppingDao
import com.example.shoppingtesting.data.local.ShoppingDatabase
import com.example.shoppingtesting.data.remote.PixaApi
import com.example.shoppingtesting.repository.ShoppingRepository
import com.example.shoppingtesting.repository.ShoppingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShoppingModule {

    @Singleton
    @Provides
    fun provideRepository(dao: ShoppingDao, api: PixaApi) =
        ShoppingRepositoryImpl(dao, api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ShoppingDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideRetrofit(): PixaApi =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
            .build().create(PixaApi::class.java)

    @Singleton
    @Provides
    fun provideShoppingDao(database: ShoppingDatabase) = database.getDao()
}