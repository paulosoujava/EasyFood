package com.paulo.easyfood.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.paulo.easyfood.R
import com.paulo.easyfood.data.dto.Meal
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.data.dto.MealsResponse
import com.paulo.easyfood.data.dto.RandomMealResponse
import com.paulo.easyfood.data.retrofit.RetrofitInstance
import com.paulo.easyfood.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RetrofitInstance.foodApi.getRandomMeal()
            .enqueue(object : Callback<RandomMealResponse>{
                override fun onResponse(
                    call: Call<RandomMealResponse>,
                    response: Response<RandomMealResponse>
                ) {
                    if(response.body() != null){
                    val randomMeal: MealDetail  = response.body()!!.meals[0]
                        Glide.with(this@HomeFragment)
                            .load(randomMeal.strMealThumb)
                            .into(binding.imgRandomMeal)
                    }else{
                        return
                    }
                }

                override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                    Log.d("TAG","ERRO")
                }

            })
    }

}