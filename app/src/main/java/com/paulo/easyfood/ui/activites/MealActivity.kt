package com.paulo.easyfood.ui.activites
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.paulo.easyfood.R
import com.paulo.easyfood.data.dto.Meal
import com.paulo.easyfood.databinding.ActivityCategoriesBinding
import com.paulo.easyfood.ui.activites.adapters.MealRecyclerAdapter
import com.paulo.easyfood.ui.activites.adapters.SetOnMealClickListener
import com.paulo.easyfood.util.Const.MEAL_ID
import com.paulo.easyfood.util.Const.MEAL_STR
import com.paulo.easyfood.util.Const.MEAL_THUMB
import com.paulo.easyfood.util.Const.CATEGORY_NAME

import com.paulo.easyfood.viewModel.MealActivityMVVM

class MealActivity : AppCompatActivity() {

    val mealActivityMvvm: MealActivityMVVM by viewModels()

    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var myAdapter: MealRecyclerAdapter
    private var categoryNme = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        startLoading()
        prepareRecyclerView()
        mealActivityMvvm.getMealsByCategory(getCategory())
        mealActivityMvvm.mutableMeal.observe(this
        ) { t ->
            if (t == null) {
                hideLoading()
                Toast.makeText(applicationContext, "No meals in this category", Toast.LENGTH_SHORT)
                    .show()
                onBackPressed()
            } else {
                myAdapter.setCategoryList(t!!)
                binding.tvCategoryCount.text = categoryNme + " : " + t.size.toString()
                hideLoading()
            }
        }

        myAdapter.setOnMealClickListener(object : SetOnMealClickListener {
            override fun setOnClickListener(meal: Meal) {
                val intent = Intent(applicationContext, MealDetailesActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_STR, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })
    }

    private fun hideLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.INVISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
        }    }

    private fun startLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.VISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.g_loading))
        }
    }

    private fun getCategory(): String {
        val x = intent.getStringExtra(CATEGORY_NAME)!!
        categoryNme = x
        return x
    }

    private fun prepareRecyclerView() {
        myAdapter = MealRecyclerAdapter()
        binding.mealRecyclerview.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }
}