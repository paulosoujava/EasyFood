package com.paulo.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paulo.easyfood.data.dto.Meal
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.data.dto.RandomMealResponse
import com.paulo.easyfood.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {

    private var _randomMealLiveData = MutableLiveData<MealDetail>()
    var randomMealLiveData = _randomMealLiveData

    fun getRandomMeal() {
        RetrofitInstance.foodApi.getRandomMeal()
            .enqueue(object : Callback<RandomMealResponse> {
                override fun onResponse(
                    call: Call<RandomMealResponse>,
                    response: Response<RandomMealResponse>
                ) {
                    if (response.body() != null) {
                        val randomMeal: MealDetail = response.body()!!.meals[0]
                        _randomMealLiveData.value = randomMeal
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                    Log.d("TAG", "ERRO")
                }

            })
    }


}
