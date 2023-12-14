/*
* @author @author Assatulla Dias (xassat00)
* @brief Implementation of showing List of Categories that user created
* */
package com.example.journalapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.CategoryAdapter
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.CategoryModel

class ListCategoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_category, container, false)
        val closeButton = view.findViewById<ImageView>(R.id.arrowBackBtn_cat_list)

        closeButton.setOnClickListener {
            // Pop the fragment from the back stack to return to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        showListCategories(view)
        return view
    }

    private fun showListCategories(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_icon)
        val layoutManager = GridLayoutManager(requireContext(), 4)
        recyclerView.layoutManager = layoutManager

        // Get all categories from the database
        val dbHandler: AppDB = AppDB(requireContext())
        val allCategories = dbHandler.viewCategories()

        // Filter out categories with IDs less than or equal to 8
        val userCategories = allCategories.filter { it.id > 8 }

        // Set up the adapter with the filtered categories
        val adapter = CategoryAdapter(userCategories, object : CategoryAdapter.OnCategoryItemClickListener {
            override fun onCategoryItemClick(category: CategoryModel) {
                // Pass the selected category to EditFragmentCategory
                val editFragment = EditFragmentCategory.newInstance(category)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, editFragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        recyclerView.adapter = adapter
    }

}