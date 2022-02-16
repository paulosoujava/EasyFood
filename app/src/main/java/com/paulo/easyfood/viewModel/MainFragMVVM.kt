package com.paulo.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.paulo.easyfood.data.dto.CategoryResponse
import com.paulo.easyfood.data.dto.MealsResponse
import com.paulo.easyfood.data.dto.RandomMealResponse
import com.paulo.easyfood.data.retrofit.FoodApi
import com.paulo.easyfood.data.retrofit.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

const val TAG = "MainMVVM"

@HiltViewModel
class MainFragMVVM @Inject constructor(
    val fooApi: FoodApi
): ViewModel() {
    private val _mutableCategory = MutableLiveData<CategoryResponse>()
    var mCategory = _mutableCategory
    private val _mutableRandomMeal = MutableLiveData<RandomMealResponse>()
    var mRandomMeal = _mutableRandomMeal
    private val _mutableMealsByCategory = MutableLiveData<MealsResponse>()
    var mMealsByCategory = _mutableMealsByCategory


    init {
        getRandomMeal()
        getAllCategories()
        getMealsByCategory("beef")
    }


    private fun getAllCategories() {
        fooApi.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                _mutableCategory.value = response.body()
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    private fun getRandomMeal() {
        fooApi.getRandomMeal().enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(call: Call<RandomMealResponse>, response: Response<RandomMealResponse>) {
                _mutableRandomMeal.value = response.body()
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    private fun getMealsByCategory(category:String) {
        fooApi.getMealsByCategory(category).enqueue(object : Callback<MealsResponse> {
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                _mutableMealsByCategory.value = response.body()
            }
            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }


}