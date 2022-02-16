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
import com.paulo.easyfood.R
import com.paulo.easyfood.data.dto.Category
import com.paulo.easyfood.databinding.FragmentCategoryBinding
import com.paulo.easyfood.ui.activites.MealActivity
import com.paulo.easyfood.ui.activites.adapters.CategoriesRecyclerAdapter
import com.paulo.easyfood.util.Const
import com.paulo.easyfood.viewModel.CategoryMVVM


class CategoryFragment : Fragment(R.layout.fragment_category) {
    private lateinit var binding:FragmentCategoryBinding
    private lateinit var myAdapter:CategoriesRecyclerAdapter
    private  val categoryMvvm:CategoryMVVM by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = CategoriesRecyclerAdapter()
        //categoryMvvm = ViewModelProviders.of(this)[CategoryMVVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeCategories()
        onCategoryClick()
    }

    private fun onCategoryClick() {
        myAdapter.onItemClicked(object : CategoriesRecyclerAdapter.OnItemCategoryClicked{
            override fun onClickListener(category: Category) {
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra(Const.CATEGORY_NAME,category.strCategory)
                startActivity(intent)
            }
        })
    }

    private fun observeCategories() {
        categoryMvvm.observeCategories().observe(viewLifecycleOwner
        ) { t -> myAdapter.setCategoryList(t!!) }
    }

    private fun prepareRecyclerView() {
        binding.favoriteRecyclerView.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
    }


}