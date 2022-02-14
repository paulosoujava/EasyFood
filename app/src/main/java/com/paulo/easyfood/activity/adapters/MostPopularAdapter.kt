package com.paulo.easyfood.activity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paulo.easyfood.data.dto.Category
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.databinding.PopularItemsBinding

class MostPopularAdapter : RecyclerView.Adapter<MostPopularAdapter.PopularMealVIewHolder>() {
    lateinit var onItemClick: (MealDetail) -> Unit
    private var mealsList = ArrayList<MealDetail>()

    fun setMeals(mealsList: ArrayList<MealDetail>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealVIewHolder {
        return PopularMealVIewHolder(
            (PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        )
    }

    override fun onBindViewHolder(holder: PopularMealVIewHolder, position: Int) {
        var meal = mealsList[position]
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.imgPopularMeal)

        holder.itemView.setOnClickListener {
            onItemClick(meal)
        }

    }

    override fun getItemCount() = mealsList.size

    class PopularMealVIewHolder(val binding: PopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}