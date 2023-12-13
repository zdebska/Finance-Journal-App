package com.example.journalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.CategoryAdapter
import com.example.journalapp.fragments.adapters.RecordAdapter
import com.example.journalapp.models.AppDB

class SelectCategoryFragment : Fragment() {
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
            override fun onCategoryItemClick(iconPath: String) {
                // Handle item click if needed
            }
        })
        recyclerView.adapter = adapter
    }
}