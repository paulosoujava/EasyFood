package com.paulo.easyfood.di

import android.content.Context
import com.paulo.easyfood.data.db.Dao
import com.paulo.easyfood.data.db.MealsDatabase
import com.paulo.easyfood.data.retrofit.FoodApi
import com.paulo.easyfood.data.retrofit.RetrofitInstance
import com.paulo.easyfood.ui.activites.adapters.CategoriesRecyclerAdapter
import com.paulo.easyfood.ui.activites.adapters.FavoriteMealsRecyclerAdapter
import com.paulo.easyfood.ui.activites.adapters.MealRecyclerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("CT_ADAPTER")
    fun provideCategoriesRecyclerAdapter(): CategoriesRecyclerAdapter {
        return CategoriesRecyclerAdapter()
    }

    @Provides
    @Singleton
    @Named("SR_ADAPTER")
    fun provideMealRecyclerAdapter(): MealRecyclerAdapter {
        return MealRecyclerAdapter()
    }

    @Provides
    @Singleton
    @Named("FA_ADAPTER")
    fun provideFavoriteMealsRecyclerAdapter(): FavoriteMealsRecyclerAdapter {
        return FavoriteMealsRecyclerAdapter()
    }


    @Provides
    @Singleton
    fun provideAppDB( @ApplicationContext context: Context): MealsDatabase{
        return MealsDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun getDao( mealsDatabase: MealsDatabase): Dao {
        return mealsDatabase.dao()
    }

    @Provides
    @Singleton
    fun provideInstanceFoodApi(): FoodApi {
        return RetrofitInstance.foodApi
    }


}