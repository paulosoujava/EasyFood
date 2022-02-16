package com.paulo.easyfood.ui.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.paulo.easyfood.R
import com.paulo.easyfood.data.dto.Meal
import com.paulo.easyfood.data.dto.MealDB
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.databinding.ActivityMealDetailesBinding
import com.paulo.easyfood.util.Const
import com.paulo.easyfood.viewModel.DetailsMVVM
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@AndroidEntryPoint
class MealDetailesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealDetailesBinding
    private var meal: Meal? = null
    private lateinit var dtMeal: MealDetail

    val detailsMVVM: DetailsMVVM by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrieving
        meal = intent.getParcelableExtra(Meal.MEAL_KEY)

        if (meal == null) finish()

        showLoading()

        setUpViewWithMealInformation()
        setFloatingButtonStatues()

        meal?.let { detailsMVVM.getMealById(it.idMeal) }

        detailsMVVM.mealDetail.observe(
            this
        ) { t ->
            setTextsInViews(t!![0])
            stopLoading()
        }

        binding.tvYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(dtMeal.strYoutube)))
        }


        binding.btnSave.setOnClickListener {
            if (isMealSavedInDatabase()) {
                deleteMeal()
                binding.btnSave.setImageResource(R.drawable.ic_baseline_save_24)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Meal was deleted",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                saveMeal()
                binding.btnSave.setImageResource(R.drawable.ic_saved)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Meal saved",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun assertNotNullIdMeal(): Boolean {
        return (meal?.idMeal != null)
    }

    private fun deleteMeal() {
        if (assertNotNullIdMeal())
            detailsMVVM.deleteMealById(meal!!.idMeal)
    }

    private fun setFloatingButtonStatues() {
        if (isMealSavedInDatabase()) {
            binding.btnSave.setImageResource(R.drawable.ic_saved)
        } else {
            binding.btnSave.setImageResource(R.drawable.ic_baseline_save_24)
        }
    }

    private fun isMealSavedInDatabase(): Boolean {
        if (assertNotNullIdMeal())
            return detailsMVVM.isMealSavedInDatabase(meal!!.idMeal)
        return false
    }

    private fun saveMeal() {
        val meal = MealDB(
            dtMeal.idMeal.toInt(),
            dtMeal.strMeal,
            dtMeal.strArea,
            dtMeal.strCategory,
            dtMeal.strInstructions,
            dtMeal.strMealThumb,
            dtMeal.strYoutube
        )

        detailsMVVM.insertMeal(meal)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.visibility = View.GONE
        binding.tvYoutube.visibility = View.INVISIBLE
    }


    private fun stopLoading() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnSave.visibility = View.VISIBLE

        binding.tvYoutube.visibility = View.VISIBLE

    }

    private fun setTextsInViews(meal: MealDetail) {
        this.dtMeal = meal
        binding.apply {
            tvInstructions.text = "- Instructions : "
            tvContent.text = meal.strInstructions
            tvAreaInfo.visibility = View.VISIBLE
            tvCategoryInfo.visibility = View.VISIBLE
            tvAreaInfo.text = tvAreaInfo.text.toString() + meal.strArea
            tvCategoryInfo.text = tvCategoryInfo.text.toString() + meal.strCategory
            tvYoutube.visibility = View.VISIBLE
        }
    }


    private fun setUpViewWithMealInformation() {
        binding.apply {
            collapsingToolbar.title = meal?.strMeal
            Glide.with(applicationContext)
                .load(meal?.strMealThumb)
                .into(imgMealDetail)
        }

    }


}