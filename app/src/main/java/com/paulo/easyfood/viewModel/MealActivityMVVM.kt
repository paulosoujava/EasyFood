package com.paulo.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paulo.easyfood.data.dto.Meal
import com.paulo.easyfood.data.dto.MealsResponse
import com.paulo.easyfood.data.retrofit.FoodApi
import com.paulo.easyfood.data.retrofit.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MealActivityMVVM @Inject constructor(
    private val foodApi: FoodApi
):ViewModel() {
    private var _mutableMeal = MutableLiveData<List<Meal>>()
    var mutableMeal = _mutableMeal

    fun getMealsByCategory(category:String){
        foodApi.getMealsByCategory(category).enqueue(object : Callback<MealsResponse>{
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                _mutableMeal.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }

        })
    }

}