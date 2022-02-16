package com.paulo.easyfood.ui.activites.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paulo.easyfood.R
import com.paulo.easyfood.data.dto.MealDB
import com.paulo.easyfood.databinding.CategoryCardBinding
import javax.inject.Inject


class FavoriteMealsRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<FavoriteMealsRecyclerAdapter.FavoriteViewHolder>() {
    private var favoriteMeals: List<MealDB> = ArrayList()
    private lateinit var onFavoriteClickListener: OnFavoriteClickListener
    private lateinit var onFavoriteLongClickListener: OnFavoriteLongClickListener

    fun setFavoriteMealsList(favoriteMeals: List<MealDB>) {
        this.favoriteMeals = favoriteMeals
        notifyDataSetChanged()
    }

    fun getMelaByPosition(position: Int):MealDB{
        return favoriteMeals[position]
    }


    fun setOnFavoriteMealClickListener(onFavoriteClickListener: OnFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener
    }

    fun setOnFavoriteLongClickListener(onFavoriteLongClickListener: OnFavoriteLongClickListener) {
        this.onFavoriteLongClickListener = onFavoriteLongClickListener
    }

    class FavoriteViewHolder(val binding: CategoryCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(CategoryCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val i = position
        holder.binding.apply {
            tvCategoryName.text = favoriteMeals[position].mealName
            Glide.with(holder.itemView)
                .load(favoriteMeals[position].mealThumb)
                .error(R.drawable.mealtest)
                .into(imgCategory)
        }

        holder.itemView.setOnClickListener {
            onFavoriteClickListener.onFavoriteClick(favoriteMeals[position])
        }

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                onFavoriteLongClickListener.onFavoriteLongCLick(favoriteMeals[i])
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return favoriteMeals.size
    }

    interface OnFavoriteClickListener {
        fun onFavoriteClick(meal: MealDB)
    }

    interface OnFavoriteLongClickListener {
        fun onFavoriteLongCLick(meal: MealDB)
    }
}