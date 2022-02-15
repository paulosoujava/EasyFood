package com.paulo.easyfood.ui.activites.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paulo.easyfood.data.dto.Meal
import com.paulo.easyfood.databinding.CategoryCardBinding

class MealRecyclerAdapter : RecyclerView.Adapter<MealRecyclerAdapter.MealViewHolder>() {

    private var mealList: List<Meal> = ArrayList()
    private lateinit var setOnMealClickListener: SetOnMealClickListener

    fun setCategoryList(mealList: List<Meal>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    fun setOnMealClickListener(setOnMealClickListener: SetOnMealClickListener) {
        this.setOnMealClickListener = setOnMealClickListener
    }

    class MealViewHolder(val binding: CategoryCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(CategoryCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.binding.apply {
            tvCategoryName.text = mealList[position].strMeal
            Glide.with(holder.itemView)
                .load(mealList[position].strMealThumb)
                .into(imgCategory)
        }

        holder.itemView.setOnClickListener {
            setOnMealClickListener.setOnClickListener(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}

interface SetOnMealClickListener {
    fun setOnClickListener(meal: Meal)
}