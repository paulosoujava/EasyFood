package com.paulo.easyfood.di

import android.content.Context
import com.paulo.easyfood.data.db.Dao
import com.paulo.easyfood.data.db.MealsDatabase
import com.paulo.easyfood.ui.activites.adapters.CategoriesRecyclerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideAppDB( @ApplicationContext context: Context): MealsDatabase{
        return MealsDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun getDao( mealsDatabase: MealsDatabase): Dao {
        return mealsDatabase.dao()
    }


}