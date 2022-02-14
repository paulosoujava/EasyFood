package com.paulo.easyfood.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paulo.easyfood.data.dto.Meal
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.data.dto.RandomMealResponse
import com.paulo.easyfood.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel : ViewModel() {
    private var _mealDetailsLiveData = MutableLiveData<MealDetail>()
    var mealDetailsLiveData = _mealDetailsLiveData

    fun getMealDetail(id: String) {
        RetrofitInstance.foodApi.getMealById(id).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(
                call: Call<RandomMealResponse>,
                response: Response<RandomMealResponse>
            ) {
                if (response.body() != null) {
                    _mealDetailsLiveData.value = response.body()!!.meals[0]
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}