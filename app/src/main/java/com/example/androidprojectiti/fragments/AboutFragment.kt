package com.example.androidprojectiti.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprojectiti.Adapters.AboutUsAdapter
import com.example.androidprojectiti.Contributor
import com.example.androidprojectiti.R

class AboutFragment : Fragment() {

    private val contributors = listOf(
        Contributor(R.drawable.marawan,"Marawan Shrief Abdelatef", "lordmarawanshrief@gmail.com", "Marawan Shrief"),
        Contributor(R.drawable.nada, "Nada Mohsen Ahmed", "mohsennada874@gmail.com", "Nada Mohsen"),
        Contributor(R.drawable.disney,"Nadra Mahmoud Saad", "oshasaad968@gmail.com", "Nadra Mahmoud"),
        Contributor(R.drawable.omar,"Omar Mahmoud Abdullah", "omar.mahmoud25102004@gmail.com", "Omar Mahmoud")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "About"
        val image : ImageView = view.findViewById(R.id.about_image_view)
        image.setImageResource(R.drawable.about)
        val recyclerView = view.findViewById<RecyclerView>(R.id.about_us_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AboutUsAdapter(contributors)
    }

}