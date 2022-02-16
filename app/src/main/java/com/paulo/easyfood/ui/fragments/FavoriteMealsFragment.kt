package com.paulo.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.snackbar.Snackbar
import com.paulo.easyfood.R
import com.paulo.easyfood.data.dto.MealDB
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.databinding.FragmentFavoriteMealsBinding
import com.paulo.easyfood.ui.MealBottomDialog
import com.paulo.easyfood.ui.activites.MealDetailesActivity
import com.paulo.easyfood.ui.activites.adapters.FavoriteMealsRecyclerAdapter
import com.paulo.easyfood.util.Const
import com.paulo.easyfood.viewModel.DetailsMVVM
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class FavoriteMeals : Fragment() {
    lateinit var recView:RecyclerView
    lateinit var fBinding:FragmentFavoriteMealsBinding

    @Inject
    @Named("FA_ADAPTER")
    lateinit var myAdapter:FavoriteMealsRecyclerAdapter
    val detailsMVVM: DetailsMVVM by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fBinding = FragmentFavoriteMealsBinding.inflate(inflater,container,false)
        return fBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView(view)
        onFavoriteMealClick()
        onFavoriteLongMealClick()
        observeBottomDialog()

        detailsMVVM.allMeals.observe(viewLifecycleOwner
        ) { t ->
            myAdapter.setFavoriteMealsList(t!!)
            if (t.isEmpty())
                fBinding.tvFavEmpty.visibility = View.VISIBLE
            else
                fBinding.tvFavEmpty.visibility = View.GONE
        }

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favoriteMeal = myAdapter.getMelaByPosition(position)
                detailsMVVM.deleteMeal(favoriteMeal)
                showDeleteSnackBar(favoriteMeal)
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recView)

    }

    private fun showDeleteSnackBar(favoriteMeal:MealDB) {
        Snackbar.make(requireView(),"Meal was deleted",Snackbar.LENGTH_LONG).apply {
            setAction("undo",View.OnClickListener {
                detailsMVVM.insertMeal(favoriteMeal)
            }).show()
        }
    }

    private fun observeBottomDialog() {
        detailsMVVM.mealBottomSheet.observe(viewLifecycleOwner,object : Observer<List<MealDetail>>{
            override fun onChanged(t: List<MealDetail>?) {
                val bottomDialog = MealBottomDialog()
                val b = Bundle()
                b.putString(Const.CATEGORY_NAME,t!![0].strCategory)
                b.putString(Const.MEAL_AREA,t[0].strArea)
                b.putString(Const.MEAL_NAME,t[0].strMeal)
                b.putString(Const.MEAL_THUMB,t[0].strMealThumb)
                b.putString(Const.MEAL_ID,t[0].idMeal)
                bottomDialog.arguments = b
                bottomDialog.show(childFragmentManager,"Favorite bottom dialog")
            }

        })
    }

    private fun prepareRecyclerView(v:View) {
        recView =v.findViewById<RecyclerView>(R.id.fav_rec_view)
        recView.adapter = myAdapter
        recView.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
    }

    private fun onFavoriteMealClick(){
        myAdapter.setOnFavoriteMealClickListener(object : FavoriteMealsRecyclerAdapter.OnFavoriteClickListener{
            override fun onFavoriteClick(meal: MealDB) {
                val intent = Intent(context, MealDetailesActivity::class.java)
                intent.putExtra(Const.MEAL_ID,meal.mealId.toString())
                intent.putExtra(Const.MEAL_STR,meal.mealName)
                intent.putExtra(Const.MEAL_THUMB,meal.mealThumb)
                startActivity(intent)
            }

        })
    }

    private fun onFavoriteLongMealClick() {
        myAdapter.setOnFavoriteLongClickListener(object : FavoriteMealsRecyclerAdapter.OnFavoriteLongClickListener{
            override fun onFavoriteLongCLick(meal: MealDB) {
                detailsMVVM.getMealByIdBottomSheet(meal.mealId.toString())
            }

        })
    }


}