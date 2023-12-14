package com.example.journalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.CategoryAdapter
import com.example.journalapp.fragments.adapters.ColorAdapter
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.CategoryModel


class EditFragmentCategory : Fragment(), CategoryAdapter.OnCategoryItemClickListener, ColorAdapter.OnColorItemClickListener {


    private var selectedIconPath: String? = "baseline_other_houses_24"
    private var selectedColorResId: Int = R.color.black
    companion object {
        private const val ARG_CATEGORY = "category"
        fun newInstance(category: CategoryModel): EditFragmentCategory {
            val fragment = EditFragmentCategory()
            val args = Bundle()
            args.putParcelable(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_category, container, false)
        val closeButton = view.findViewById<ImageView>(R.id.arrowBackBtn_cat_edit)
        val deleteButton = view.findViewById<Button>(R.id.deleteCategoryButton_edit)
        val saveButton = view.findViewById<Button>(R.id.saveCategoryButton_edit)

        val category: CategoryModel? = arguments?.getParcelable(ARG_CATEGORY)
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

        closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        deleteButton.setOnClickListener {
            deleteCategory()
        }

        saveButton.setOnClickListener {
            saveCategory()
        }

        if (category != null) {
            val editText = view?.findViewById<EditText>(R.id.editSelectCategory)
            val drawable = ContextCompat.getDrawable(requireContext(), getResourceId(category.iconPath))
            drawable?.setTint(ContextCompat.getColor(requireContext(), category.colorResId))
            editText?.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null)
            editText?.hint = category.name
            selectedColorResId = category.colorResId
            selectedIconPath = category.iconPath
        }

        showColors(view, defaultColors)
        showCategoriesList(view)
        return view
    }

    private fun deleteCategory() {
        val category: CategoryModel? = arguments?.getParcelable(ARG_CATEGORY)
        if (category != null) {
            // Delete the category from the database
            val dbHandler: AppDB = AppDB(requireContext())
            val success = dbHandler.deleteCategory(category.id)

            if (success > 0) {
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                // Error handling
            }
        }
    }

    private fun saveCategory() {
        val category: CategoryModel? = arguments?.getParcelable(ARG_CATEGORY)
        if (category != null) {

            val editText = view?.findViewById<EditText>(R.id.editSelectCategory)
            var categoryName = editText?.text.toString()
            if (categoryName == "") {
                categoryName = category.name
            }

            val updatedCategory = CategoryModel(
                id = category.id,
                name = categoryName,
                iconPath = selectedIconPath!!,
                colorResId = selectedColorResId
            )

            val dbHandler: AppDB = AppDB(requireContext())
            val success = dbHandler.updateCategory(updatedCategory)

            if (success > 0) {
                Toast.makeText(requireContext(), "Category changed successfully", Toast.LENGTH_SHORT).show()
                editText?.setText("")
                selectedIconPath = null
                selectedColorResId = R.color.black
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                // Error handling
            }
        }
    }


    override fun onCategoryItemClick(category: CategoryModel) {
        selectedIconPath = category.iconPath
        val editText = view?.findViewById<EditText>(R.id.editSelectCategory)
        editText?.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(requireContext(), getResourceId(category.iconPath)),
            null,
            null,
            null
        )
    }

    override fun onColorItemClick(colorResId: Int) {
        selectedColorResId = colorResId
        // Handle color item click
        val editText = view?.findViewById<EditText>(R.id.editSelectCategory)
        val currentDrawable = editText?.compoundDrawables?.get(0)
        if (currentDrawable != null) {
            val newDrawable = currentDrawable.constantState?.newDrawable()?.mutate()
            newDrawable?.setTint(ContextCompat.getColor(requireContext(), colorResId))

            // Set the new drawable with tint to the EditText
            editText.setCompoundDrawablesWithIntrinsicBounds(newDrawable, null, null, null)
        }
    }
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
    private fun getResourceId(iconPath: String): Int {
        // Convert iconPath to resource ID using resources
        return resources.getIdentifier(iconPath, "drawable", requireContext().packageName)
    }

}