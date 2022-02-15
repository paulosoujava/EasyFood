package com.paulo.easyfood.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paulo.easyfood.R
import com.paulo.easyfood.ui.activites.MealDetailesActivity
import com.paulo.easyfood.util.Const

class MealBottomDialog() : BottomSheetDialogFragment() {
    private var mealName = ""
    private var mealId =""
    private var mealImg = ""
    private var mealCountry = ""
    private var mealCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.AppBottomSheetDialogTheme)
        val b = arguments
        mealName = b!!.getString(Const.MEAL_NAME).toString()
        mealId =b!!.getString(Const.MEAL_ID).toString()
        mealImg =b!!.getString(Const.MEAL_THUMB).toString()
        mealCategory =b!!.getString(Const.CATEGORY_NAME).toString()
        mealCountry =b!!.getString(Const.MEAL_AREA).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareView(view)

        view.setOnClickListener {
            val intent = Intent(context, MealDetailesActivity::class.java)
            intent.putExtra(Const.MEAL_ID,mealId)
            intent.putExtra(Const.MEAL_STR,mealName)
            intent.putExtra(Const.MEAL_THUMB,mealImg)
            startActivity(intent)
        }

    }

    private fun prepareView(view:View){
        val tvMealName = view.findViewById<TextView>(R.id.tv_meal_name_in_btmsheet)
        val tvMealCategory = view.findViewById<TextView>(R.id.tv_meal_category)
        val tvMealCountry = view.findViewById<TextView>(R.id.tv_meal_country)
        val imgMeal = view.findViewById<ImageView>(R.id.img_category)

        Glide.with(view)
            .load(mealImg)
            .into(imgMeal)
        tvMealName.text = mealName
        tvMealCategory.text = mealCategory
        tvMealCountry.text = mealCountry
    }


}