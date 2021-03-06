package com.paulo.easyfood.ui.fragments
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.paulo.easyfood.data.dto.MealDetail
import com.paulo.easyfood.databinding.FragmentSearchBinding
import com.paulo.easyfood.ui.activites.MealDetailesActivity
import com.paulo.easyfood.ui.activites.adapters.MealRecyclerAdapter
import com.paulo.easyfood.util.Const
import com.paulo.easyfood.viewModel.SearchMVVM
import javax.inject.Inject
import javax.inject.Named


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var mealId = ""
    private var mealStr = ""
    private var mealThub = ""

   private val searchMvvm: SearchMVVM by viewModels()

    @Inject
    @Named("SR_ADAPTER")
    lateinit var myAdapter: MealRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSearchClick()
        observeSearchLiveData()
        setOnMealCardClick()
    }

    private fun setOnMealCardClick() {
        binding.searchedMealCard.setOnClickListener {
            val intent = Intent(context, MealDetailesActivity::class.java)

            intent.putExtra(Const.MEAL_ID, mealId)
            intent.putExtra(Const.MEAL_STR, mealStr)
            intent.putExtra(Const.MEAL_THUMB, mealThub)

            startActivity(intent)


        }
    }

    private fun onSearchClick() {
        binding.icSearch.setOnClickListener {
            searchMvvm.searchMealDetail(binding.edSearch.text.toString(),context)

        }
    }

    private fun observeSearchLiveData() {
        searchMvvm.observeSearchLiveData()
            .observe(viewLifecycleOwner
            ) { t ->
                if (t == null) {
                    Toast.makeText(context, "No such a meal", Toast.LENGTH_SHORT).show()
                } else {
                    binding.apply {

                        mealId = t.idMeal
                        mealStr = t.strMeal
                        mealThub = t.strMealThumb

                        Glide.with(requireContext().applicationContext)
                            .load(t.strMealThumb)
                            .into(imgSearchedMeal)

                        tvSearchedMeal.text = t.strMeal
                        searchedMealCard.visibility = View.VISIBLE
                    }
                }
            }
    }


}