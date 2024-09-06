package com.example.androidprojectiti.aboutUs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val bottomNavBar = requireActivity().findViewById<View>(R.id.bottom_nav)
        bottomNavBar.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)
        val image : ImageView = view.findViewById(R.id.about_image_view)
        image.setImageResource(R.drawable.about)
        val recyclerView = view.findViewById<RecyclerView>(R.id.about_us_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AboutUsAdapter(contributors)
    }
    override fun onDestroyView() {
        super.onDestroyView()

        val currentDestination = findNavController().currentDestination?.id

        when(currentDestination){
            R.id.aboutFragment ->{
                // Don't Do anything
            }
            else -> {
                val bottomNavBar = requireActivity().findViewById<View>(R.id.bottom_nav)
                bottomNavBar.visibility = View.VISIBLE
                (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()  // Navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}