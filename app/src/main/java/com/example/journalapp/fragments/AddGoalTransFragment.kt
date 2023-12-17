/*
 * @author Zdebska Kateryna (xzdebs00)
 * @brief A fragment that shows and sets the "add goal transaction" page
 */

package com.example.journalapp.fragments

import SharedViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.journalapp.R
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.GoalModel
import com.example.journalapp.models.GoalTransactionModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A fragment that displays and manages the "add goal transaction" page.
 */
class AddGoalTransFragment : Fragment() {
    private var goalID: Int = -1 // Initialize with a default value
    private val sharedViewModel: SharedViewModel by lazy {
        SharedViewModel.getInstance(requireActivity().application)
    }
    private lateinit var view: View

    companion object {
        private const val ARG_DATA = "data"

        /**
         * Create a new instance of AddGoalTransFragment with the specified data.
         */
        fun newInstance(data: GoalModel): AddGoalTransFragment {
            val fragment = AddGoalTransFragment()
            val args = Bundle()
            args.putParcelable(ARG_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_goal_transaction, container, false)
        val arrowBackBtn = view.findViewById<ImageView>(R.id.arrowBackBtn)
        val amountBtn = view.findViewById<EditText>(R.id.SelectAmountBtn)
        val amountImg = view.findViewById<ImageView>(R.id.SelectAmountImg)

        val saveBtn = view.findViewById<Button>(R.id.saveGoalTransBtn)

        // Get the current date and set it to the button Date as a text
        val calendar = Calendar.getInstance()
        val currentDate = Date(calendar.timeInMillis)
        // Format the date as a string and pass it as text to the button
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val formattedDate = dateFormat.format(currentDate)

        // Set up the input fields and buttons
        setTextVal(amountBtn, amountImg)
        sharedViewModel.selectedGoal.observe(viewLifecycleOwner) { goal ->
            // Update goalID when the selected goal changes
            goalID = goal?.id ?: -1
        }

        // Save transaction to DB on button click
        saveBtn.setOnClickListener() {
            val status = addRecord(view, formattedDate, amountBtn, goalID)
            if (status == 1) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        // Handle the back button click
        arrowBackBtn.setOnClickListener {
            // Pop the fragment from the back stack to return to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }

    /**
     * Set up the input field and image view for user input.
     */
    private fun setTextVal(Btn: EditText, Img: ImageView) {
        if (Btn.id == R.id.SelectAmountBtn) {
            Btn.inputType = InputType.TYPE_CLASS_NUMBER
        }
        for (i in listOf(Btn, Img)) {
            i.setOnClickListener() {
                // Open the keyboard
                val inputMethodManager =
                    Btn.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(Btn, InputMethodManager.SHOW_IMPLICIT)

                val newText = Btn.text.toString()
                Btn.setText(newText)
                Btn.setSelection(Btn.text.length)
            }
        }
        // Close the keyboard
        Btn.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Close the keyboard when "OK" is clicked
                val inputMethodManager =
                    Btn.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(Btn.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    /**
     * Add a new transaction record to the database.
     */
    @SuppressLint("SetTextI18n")
    private fun addRecord(
        view: View, date: String, amountBtn: EditText, goalId: Int
    ): Int {

        val recDate = date
        val recAmount = amountBtn.text.toString()

        val dbHandler: AppDB = AppDB(requireContext())

        if (recAmount.isNotEmpty()) {
            val newRecord = GoalTransactionModel(
                0, recAmount.toFloat(), recDate, goalId
            )
            val status = dbHandler.addGoalTransaction(newRecord)

            if (status > -1) {
                // Show a success message and clear the input field
                Toast.makeText(requireContext(), "Record saved", Toast.LENGTH_LONG).show()
                amountBtn.text.clear()
                return 1
            }
        } else {
            // Show an error message if the amount is blank
            Toast.makeText(
                requireContext(),
                "Amount cannot be blank",
                Toast.LENGTH_LONG
            ).show()
            return 0
        }
        return 0
    }
}
