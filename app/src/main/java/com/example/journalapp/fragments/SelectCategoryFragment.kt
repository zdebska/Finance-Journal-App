/*
* @author @author Assatulla Dias (xassat00)
* @brief Implementation of selecting Category from available categories
* */
package com.example.journalapp.fragments

import SharedViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.CategoryAdapter
import com.example.journalapp.fragments.adapters.RecordAdapter
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.CategoryModel

class SelectCategoryFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by lazy {
        SharedViewModel.getInstance(requireActivity().application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_category, container, false)

        val closeButton = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.closeCatButton)
        val addButton = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.addCatButton)
        val textAddButton = view.findViewById<TextView>(R.id.addCatTextView)
        val editButton = view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.editCatButton)
        val textEditButton = view.findViewById<TextView>(R.id.editCatTextView)

        editButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val editCategory = ListCategoryFragment()

            fragmentTransaction.add(R.id.main_container, editCategory)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        textEditButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val editCategory = ListCategoryFragment()

            fragmentTransaction.add(R.id.main_container, editCategory)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        textAddButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val addCategory = AddCategoryFragment()

            fragmentTransaction.add(R.id.main_container, addCategory)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        addButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val addCategory = AddCategoryFragment()

            fragmentTransaction.add(R.id.main_container, addCategory)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        closeButton.setOnClickListener {
            // Pop the fragment from the back stack to return to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        showCategories(view)
        return view
    }

    private fun showCategories(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_cat)
        val itemsInRow = 3
        val layoutManager = GridLayoutManager(requireContext(), itemsInRow)
        recyclerView.layoutManager = layoutManager
        val dbHandler: AppDB = AppDB(requireContext())
        val records = dbHandler.viewCategories()
        val adapter = CategoryAdapter(records, object : CategoryAdapter.OnCategoryItemClickListener {
            override fun onCategoryItemClick(category: CategoryModel) {
                sharedViewModel.selectedCategory = category
                requireActivity().supportFragmentManager.popBackStack()
            }
        })
        recyclerView.adapter = adapter
    }
}