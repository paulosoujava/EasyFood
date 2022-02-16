package com.paulo.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.paulo.easyfood.R
import com.paulo.easyfood.data.dto.*

import com.paulo.easyfood.databinding.FragmentHomeBinding
import com.paulo.easyfood.ui.MealBottomDialog
import com.paulo.easyfood.ui.activites.MealActivity
import com.paulo.easyfood.ui.activites.MealDetailesActivity
import com.paulo.easyfood.ui.activites.adapters.CategoriesRecyclerAdapter
import com.paulo.easyfood.ui.activites.adapters.MostPopularRecyclerAdapter
import com.paulo.easyfood.ui.activites.adapters.OnItemClick
import com.paulo.easyfood.ui.activites.adapters.OnLongItemClick
import com.paulo.easyfood.util.Const
import com.paulo.easyfood.viewModel.DetailsMVVM
import com.paulo.easyfood.viewModel.MainFragMVVM
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var randomMealId = ""

    val detailMvvm: DetailsMVVM by viewModels()
    val mainFragMVVM: MainFragMVVM by viewModels()

    lateinit var meal: RandomMealResponse
    lateinit var binding: FragmentHomeBinding

    @Inject
    @Named("CT_ADAPTER")
    lateinit var myAdapter: CategoriesRecyclerAdapter
    @Inject
    lateinit var mostPopularFoodAdapter: MostPopularRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingCase()


        prepareCategoryRecyclerView()
        preparePopularMeals()
        onRandomMealClick()
        onRandomLongClick()


        mainFragMVVM.mMealsByCategory.observe(
            viewLifecycleOwner
        ) { t ->
            val meals = t!!.meals
            setMealsByCategoryAdapter(meals)
            cancelLoadingCase()
        }

        mainFragMVVM.mCategory.observe(
            viewLifecycleOwner
        ) { t ->
            val categories = t!!.categories
            setCategoryAdapter(categories)
        }

        mainFragMVVM.mRandomMeal.observe(
            viewLifecycleOwner
        ) { t ->
            val mealImage = view.findViewById<ImageView>(R.id.img_random_meal)
            val imageUrl = t!!.meals[0].strMealThumb
            randomMealId = t.meals[0].idMeal
            Glide.with(this@HomeFragment)
                .load(imageUrl)
                .into(mealImage)
            meal = t
        }

        mostPopularFoodAdapter.setOnClickListener(object : OnItemClick {
            override fun onItemClick(meal: Meal) {
                val intent = Intent(activity, MealDetailesActivity::class.java)
                intent.putExtra(Meal.MEAL_KEY, meal)
                startActivity(intent)
            }

        })

        myAdapter.onItemClicked(object : CategoriesRecyclerAdapter.OnItemCategoryClicked {
            override fun onClickListener(category: Category) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(Const.CATEGORY_NAME, category.strCategory)
                startActivity(intent)
            }

        })

        mostPopularFoodAdapter.setOnLongCLickListener(object : OnLongItemClick {
            override fun onItemLongClick(meal: Meal) {
                detailMvvm.getMealByIdBottomSheet(meal.idMeal)
            }

        })

        detailMvvm.mealBottomSheet
            .observe(
                viewLifecycleOwner
            ) { t ->
                val bottomSheetFragment = MealBottomDialog()
                val b = Bundle()
                b.putString(Const.CATEGORY_NAME, t!![0].strCategory)
                b.putString(Const.MEAL_AREA, t[0].strArea)
                b.putString(Const.MEAL_NAME, t[0].strMeal)
                b.putString(Const.MEAL_THUMB, t[0].strMealThumb)
                b.putString(Const.MEAL_ID, t[0].idMeal)

                bottomSheetFragment.arguments = b

                bottomSheetFragment.show(childFragmentManager, "BottomSheetDialog")
            }


        // on search icon click
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }


    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            val temp = meal.meals[0]
            val intent = Intent(activity, MealDetailesActivity::class.java)
            intent.putExtra(Const.MEAL_ID, temp.idMeal)
            intent.putExtra(Const.MEAL_STR, temp.strMeal)
            intent.putExtra(Const.MEAL_THUMB, temp.strMealThumb)
            startActivity(intent)
        }

    }

    private fun onRandomLongClick() {

        binding.randomMeal.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                detailMvvm.getMealByIdBottomSheet(randomMealId)
                return true
            }

        })
    }

    private fun showLoadingCase() {
        binding.apply {
            header.visibility = View.INVISIBLE

            tvWouldLikeToEat.visibility = View.INVISIBLE
            randomMeal.visibility = View.INVISIBLE
            tvOverPupItems.visibility = View.INVISIBLE
            recViewMealsPopular.visibility = View.INVISIBLE
            tvCategory.visibility = View.INVISIBLE
            categoryCard.visibility = View.INVISIBLE
            loadingGif.visibility = View.VISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.g_loading))

        }
    }

    private fun cancelLoadingCase() {
        binding.apply {
            header.visibility = View.VISIBLE
            tvWouldLikeToEat.visibility = View.VISIBLE
            randomMeal.visibility = View.VISIBLE
            tvOverPupItems.visibility = View.VISIBLE
            recViewMealsPopular.visibility = View.VISIBLE
            tvCategory.visibility = View.VISIBLE
            categoryCard.visibility = View.VISIBLE
            loadingGif.visibility = View.INVISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        }
    }

    private fun setMealsByCategoryAdapter(meals: List<Meal>) {
        mostPopularFoodAdapter.setMealList(meals)
    }

    private fun setCategoryAdapter(categories: List<Category>) {
        myAdapter.setCategoryList(categories)
    }

    private fun prepareCategoryRecyclerView() {
        binding.recyclerView.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun preparePopularMeals() {
        binding.recViewMealsPopular.apply {
            adapter = mostPopularFoodAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        }
    }

}