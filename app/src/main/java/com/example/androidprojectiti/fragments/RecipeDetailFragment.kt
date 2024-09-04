package com.example.androidprojectiti.fragments

import android.annotation.SuppressLint
import android.content.Context
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
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.Repositry.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment() {

//    private var stringJavaScript : String = ""
    private val args by navArgs<RecipeDetailFragmentArgs>()
    private lateinit var image : ImageView
    private lateinit var title : TextView
    private lateinit var detailText : TextView
    private lateinit var category : TextView
    private lateinit var showMoreAndLess : TextView
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var heart : ImageButton
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
        youTubePlayerView = view.findViewById(R.id.web_view)
        heart = view.findViewById(R.id.recipe_details_heart)


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
        val video : String  = meal.strYoutube.toString()

        playVideo(video)
        detailText.text = detailText.text.toString() + decreaseText(fullText)
        showMoreAndLess.setOnClickListener{
            handleTextView(fullText)
        }

        val sharedPreferences = requireActivity().getSharedPreferences("logging_details", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "guest")

        lifecycleScope.launch {
            val userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
            val favoriteMeals = userRepo.getUserFavoriteMeals(email ?: "guest")
            var isFavorite = favoriteMeals.contains(meal)

            if (isFavorite)
                heart.setImageResource(R.drawable.red_heart)
            else
                heart.setImageResource(R.drawable.white_heart)


            heart.setOnClickListener {
                if (isFavorite) {
                    heart.setImageResource(R.drawable.white_heart)
                    lifecycleScope.launch {
                        userRepo.deleteMealFromFav(UserFavorites(email ?: "guest", meal))
                        Toast.makeText(
                            view.context,
                            "${meal.strMeal} removed from favorites",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
                else {
                    heart.setImageResource(R.drawable.red_heart)
                    lifecycleScope.launch {
                        userRepo.insertMealToFav(UserFavorites(email ?: "guest", meal))
                        Toast.makeText(
                            view.context,
                            "${meal.strMeal} added to favorites",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
                isFavorite = !isFavorite
            }

        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Details"
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


    private fun playVideo(video : String){

        val result = video.substring(video.lastIndexOf('=') + 1)

        // object : AbstractYouTubePlayerListener(): In Kotlin, you create an anonymous class that extends AbstractYouTubePlayerListener using the object keyword
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
//                val videoId = result  // "S0Q4gqBUs7c"
                youTubePlayer.loadVideo(result, 0f)
            }
        })


    }



}