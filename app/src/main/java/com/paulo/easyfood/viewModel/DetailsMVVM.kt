package com.paulo.easyfood.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.paulo.easyfood.data.db.MealsDatabase
import com.paulo.easyfood.data.db.Repository
import com.paulo.easyfood.data.dto.MealDB
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.data.dto.RandomMealResponse
import com.paulo.easyfood.data.retrofit.FoodApi
import com.paulo.easyfood.data.retrofit.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailsMVVM @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val foodApi: FoodApi
) : AndroidViewModel(application) {

    private val _mutableMealDetail = MutableLiveData<List<MealDetail>>()
    private val _mutableMealBottomSheet = MutableLiveData<List<MealDetail>>()
    private var _allMeals: LiveData<List<MealDB>> = repository.mealList

    //PUBLICS
    var mealDetail = _mutableMealDetail
    var mealBottomSheet = _mutableMealBottomSheet
    var allMeals = _allMeals

    fun insertMeal(meal: MealDB) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoriteMeal(meal)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun deleteMeal(meal: MealDB) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMeal(meal)
    }

    fun getMealById(id: String) {
        foodApi.getMealById(id).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(
                call: Call<RandomMealResponse>,
                response: Response<RandomMealResponse>
            ) {
                _mutableMealDetail.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun isMealSavedInDatabase(mealId: String): Boolean {
        var meal: MealDB? = null
        runBlocking(Dispatchers.IO) {
            meal = repository.getMealById(mealId)
        }
        if (meal == null)
            return false
        return true

    }

    fun deleteMealById(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMealById(mealId)
        }
    }

    fun getMealByIdBottomSheet(id: String) {
        foodApi.getMealById(id).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(
                call: Call<RandomMealResponse>,
                response: Response<RandomMealResponse>
            ) {
                _mutableMealBottomSheet.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }


}