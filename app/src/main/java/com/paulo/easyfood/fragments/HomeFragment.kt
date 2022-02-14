package com.paulo.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.paulo.easyfood.R
import com.paulo.easyfood.activity.MealActivity
import com.paulo.easyfood.activity.adapters.CategoriesAdapter
import com.paulo.easyfood.activity.adapters.MostPopularAdapter
import com.paulo.easyfood.data.dto.Category
import com.paulo.easyfood.data.dto.Meal
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.databinding.FragmentHomeBinding
import com.paulo.easyfood.viewModel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var randomMeal: MealDetail
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoryAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example/easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.example/easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example/easyfood.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]
        popularItemsAdapter = MostPopularAdapter()
        categoryAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        homeViewModel.getCategoriesItems()
        observePopularItemsLiveData()

        popularItemsRecyclerView()
        categoryItemsRecyclerView()
        getCategories()

        popularItemClick()

    }

    private fun getCategories() {
        homeViewModel.categoryMealsLiveData.observe(viewLifecycleOwner) {
            categoryAdapter.setCategoryList(it)
        }
    }

    private fun popularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strCategory)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun categoryItemsRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                activity,
                2,
                    LinearLayoutManager.VERTICAL, false
            )
            adapter = categoryAdapter
        }
    }

    private fun popularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        homeViewModel.popularMealsLiveData.observe(viewLifecycleOwner) {
            popularItemsAdapter.setMeals(it as ArrayList<MealDetail>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }


    private fun observerRandomMeal() {
        homeViewModel.randomMealLiveData.observe(
            viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal?.strMealThumb)
                .error(R.drawable.mealtest)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        }
    }

}