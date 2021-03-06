package com.paulo.easyfood.viewModel


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.data.dto.RandomMealResponse
import com.paulo.easyfood.data.retrofit.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchMVVM @Inject constructor(): ViewModel() {
    private var searchedMealLiveData = MutableLiveData<MealDetail>()


    fun searchMealDetail(name: String,context: Context?) {
        RetrofitInstance.foodApi.getMealByName(name).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(call: Call<RandomMealResponse>, response: Response<RandomMealResponse>) {
                if (response.body()?.meals == null)
                    Toast.makeText(context?.applicationContext, "No such a meal", Toast.LENGTH_SHORT).show()
                else
                    searchedMealLiveData.value = response.body()!!.meals[0]
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observeSearchLiveData(): LiveData<MealDetail> {
        return searchedMealLiveData
    }
}