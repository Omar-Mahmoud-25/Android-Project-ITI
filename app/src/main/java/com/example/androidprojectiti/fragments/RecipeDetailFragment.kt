package com.example.androidprojectiti.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.androidprojectiti.R

class RecipeDetailFragment : Fragment() {

    private val args by navArgs<RecipeDetailFragmentArgs>()
    private lateinit var image : ImageView
    private lateinit var title : TextView
    private lateinit var detailText : TextView
    private lateinit var category : TextView
    private lateinit var showMoreAndLess : TextView
    private var isShowLess : Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        image = view.findViewById(R.id.image_view_recipe_details)
        title = view.findViewById(R.id.text_view_title)
        detailText = view.findViewById(R.id.text_view_details)
        category = view.findViewById(R.id.text_view_category)
        showMoreAndLess = view.findViewById(R.id.text_view_show)

        val meal = args.favMeal

        Glide.with(this)
            .load(meal.strMealThumb)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_arrow_circle_down_24)
                    .error(R.drawable.baseline_error_24)
            )
            .into(image)

        title.text = meal.strMeal
        category.text =  category.text.toString() + meal.strCategory
        val fullText = meal.strInstructions.toString()

        detailText.text = detailText.text.toString() + decreaseText(fullText)
        showMoreAndLess.setOnClickListener{
            handleTextView(fullText)
        }

        return view
    }


    @SuppressLint("SetTextI18n")
    private fun handleTextView(fullText: String) {
        if (isShowLess){
            detailText.text = decreaseText(fullText)
            showMoreAndLess.text = "Show More"
        }
        else {
            detailText.text = fullText
            showMoreAndLess.text = "Show Less"
        }
        isShowLess = !isShowLess
    }

    private fun decreaseText (text : String) : String{
        val words = text.split(" ")
        return if (words.size > 15)
            words.take(15).joinToString (" ") + "..."
        else
            text

    }




}