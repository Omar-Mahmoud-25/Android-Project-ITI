package com.example.androidprojectiti.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.androidprojectiti.R

class RecipeDetailFragment : Fragment() {

    private val args by navArgs<RecipeDetailFragmentArgs>()
    private lateinit var image : ImageView
    private lateinit var title : TextView
    private lateinit var detailText : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        image = view.findViewById(R.id.image_view_recipe_details)
        title = view.findViewById(R.id.text_view_title)
        detailText = view.findViewById(R.id.text_view_details)

        val meal = args.favMeal

        Glide.with(this)
            .load(meal.strImageSource)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_arrow_circle_down_24)
                    .error(R.drawable.baseline_error_24)
            )
            .into(image)

        title.text = meal.strMeal
        detailText.text = meal.strInstructions

        return view
    }

}