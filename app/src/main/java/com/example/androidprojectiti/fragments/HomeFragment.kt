package com.example.androidprojectiti.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprojectiti.Adapters.CategoryAdapter
import com.example.androidprojectiti.R
import com.example.androidprojectiti.category.categoryRepoImp
import com.example.androidprojectiti.dto.Category
import com.example.androidprojectiti.network.ApiClient
import com.example.androidprojectiti.viewModels.Home.FactoryClassHome
import com.example.androidprojectiti.viewModels.Home.HomeViewModel

class HomeFragment : Fragment() {
    lateinit var list_of_Categories: List<Category>
    private lateinit var retrofitViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factoryClass = FactoryClassHome(
            categoryRepositry = categoryRepoImp(
                remoteDataSource = ApiClient
            )
        )
        retrofitViewModel = ViewModelProvider(this, factoryClass)
            .get(HomeViewModel::class.java)


        retrofitViewModel.getCategories()

        val list = view.findViewById<RecyclerView>(R.id.category_recycler_view)
        retrofitViewModel.CategoryList.observe(viewLifecycleOwner) {
            val adapter = CategoryAdapter(it)
            list_of_Categories = it
            list.adapter = adapter
            list.layoutManager = LinearLayoutManager(requireContext())
Log.d("nada",list.size.toString())
        }
    }
}