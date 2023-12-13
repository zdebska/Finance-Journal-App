package com.example.journalapp.fragments

import android.graphics.PorterDuffColorFilter
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.CategoryAdapter
import com.example.journalapp.models.AppDB
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.example.journalapp.fragments.adapters.ColorAdapter
import com.example.journalapp.models.CategoryModel

class AddCategoryFragment : Fragment(), CategoryAdapter.OnCategoryItemClickListener, ColorAdapter.OnColorItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var selectedIconPath: String? = null
    private var selectedColorResId: Int = R.color.black
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_category, container, false)
        val defaultColors = listOf(
            android.R.color.holo_red_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_purple,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.black,
            android.R.color.holo_red_dark,
            android.R.color.holo_blue_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            R.color.colorSecond,
            R.color.blue,
            R.color.pink,
            R.color.orange,
        )


        val closeButton = view.findViewById<ImageView>(R.id.arrowBackBtn_cat)
        val saveButton = view.findViewById<Button>(R.id.saveCategoryButton)


        saveButton.setOnClickListener {
            // Handle the Save button click
            saveCategory()
        }
        closeButton.setOnClickListener {
            // Pop the fragment from the back stack to return to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }
        showColors(view, defaultColors)
        showCategoriesList(view)
        return view
    }

    private fun saveCategory() {
        val categoryNameEditText = view?.findViewById<EditText>(R.id.editSelectCategory)
        val categoryName = categoryNameEditText?.text.toString().trim()

        if (categoryName.isNotEmpty() && selectedIconPath != null) {
            val dbHandler: AppDB = AppDB(requireContext())

            val newCategory = CategoryModel(name = categoryName, iconPath = selectedIconPath!!, colorResId = selectedColorResId)

            // Add the new category to the database
            val success = dbHandler.addCategory(newCategory)

            if (success > 0) {
                // Category added successfully
                Toast.makeText(requireContext(), "Category added successfully", Toast.LENGTH_SHORT).show()

                // Optionally, you can clear the entered category name and selected icon
                categoryNameEditText?.setText("")
                selectedIconPath = null
                selectedColorResId = R.color.black
            } else {
                // Failed to add category
                Toast.makeText(requireContext(), "Failed to add category", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Display a message if the category name or icon is not selected
            Toast.makeText(requireContext(), "Please enter category name and select an icon", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCategoryItemClick(iconPath: String) {
        selectedIconPath = iconPath
        // Update the EditText with the selected category icon drawable
        val editText = view?.findViewById<EditText>(R.id.editSelectCategory)
        editText?.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(requireContext(), getResourceId(iconPath)),
            null,
            null,
            null
        )
    }

    override fun onColorItemClick(colorResId: Int) {
        selectedColorResId = colorResId
        // Handle color item click
        val editText = view?.findViewById<EditText>(R.id.editSelectCategory)
        val currentDrawable = editText?.compoundDrawables?.get(0) // assuming the icon is set at the start
        // Check if the current drawable is not null
        if (currentDrawable != null) {
            // Create a new instance of the drawable with the updated tint
            val newDrawable = currentDrawable.constantState?.newDrawable()?.mutate()
            newDrawable?.setTint(ContextCompat.getColor(requireContext(), colorResId))

            // Set the new drawable with tint to the EditText
            editText.setCompoundDrawablesWithIntrinsicBounds(newDrawable, null, null, null)
        }
    }

    private fun getResourceId(iconPath: String): Int {
        // Convert iconPath to resource ID using resources
        return resources.getIdentifier(iconPath, "drawable", requireContext().packageName)
    }

    // Inside your AddCategoryFragment
    private fun showColors(view: View, colors: List<Int>) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_color)
        val itemsInRow = 8
        val layoutManager = GridLayoutManager(requireContext(), itemsInRow)
        recyclerView.layoutManager = layoutManager

        val colorAdapter = ColorAdapter(colors, this)
        recyclerView.adapter = colorAdapter
    }

    private fun showCategoriesList(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_icon)
        val itemsInRow = 4
        val layoutManager = GridLayoutManager(requireContext(), itemsInRow)
        recyclerView.layoutManager = layoutManager
        val dbHandler: AppDB = AppDB(requireContext())
        val records = dbHandler.viewCategories().filter { it.id <= 8 }
        val adapter = CategoryAdapter(records, this)

        recyclerView.adapter = adapter
    }
}