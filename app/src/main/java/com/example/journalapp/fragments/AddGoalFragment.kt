/*
 * @author Zdebska Kateryna (xzdebs00)
 * @brief A fragment that shows and sets the "add goal" page
 */

package com.example.journalapp.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A fragment that displays and manages the "add goal" page.
 */
class AddGoalFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_goal, container, false)

        val arrowBackBtn = view.findViewById<ImageView>(R.id.arrowBackBtn)
        val dateBtn = view.findViewById<Button>(R.id.SelectDateBtn)
        val dateImg = view.findViewById<ImageView>(R.id.SelectDateImg)

        val amountBtn = view.findViewById<EditText>(R.id.SelectAmountBtn)
        val amountImg = view.findViewById<ImageView>(R.id.SelectAmountImg)

        val nameBtn = view.findViewById<EditText>(R.id.SelectNameBtn)
        val nameImg = view.findViewById<ImageView>(R.id.SelectNameImg)

        val saveBtn = view.findViewById<Button>(R.id.saveGoalBtn)

        // Get the current date and set it to the button Date as a text
        val calendar = Calendar.getInstance()
        setTomorrowDate(dateBtn, calendar)

        // Set listener to the date button to open calendar onclick
        dateSelector(dateBtn, calendar, dateImg)

        // Set entered amount of money by a user to the "Amount" field
        setTextVal(amountBtn, amountImg)

        // Set entered note by a user to the "Note" field
        setTextVal(nameBtn, nameImg)

        // Save transaction to DB
        saveBtn.setOnClickListener() {
            val status = addGoal(dateBtn, amountBtn, nameBtn)
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
     * Set tomorrow as a default date when creating a new transaction record.
     */
    private fun setTomorrowDate(dateBtn: Button, calendar: Calendar) {
        // Add 1 day to the current date
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        // Get the date after adding 1 day
        val tomorrowDate = Date(calendar.timeInMillis)

        // Format the date as a string and pass it as text to the button
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val formattedTomorrowDate = dateFormat.format(tomorrowDate)

        // Set the formatted date to the button text
        dateBtn.text = formattedTomorrowDate
    }

    /**
     * Set date selector to be shown on date button click.
     */
    private fun dateSelector(dateBtn: Button, calendar: Calendar, dateImg: ImageView) {
        for (i in listOf<View>(dateBtn, dateImg)) {
            i.setOnClickListener() {

                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { view, selectedYear, selectedMonth, selectedDay ->
                        // Handle the selected date here
                        val selectedDate =
                            "$selectedDay/${selectedMonth + 1}/$selectedYear" // Month is 0-based
                        dateBtn.text = selectedDate // Set the selected date to the button text
                    }, year, month, day
                )
                datePickerDialog.show()
            }
        }
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
                // Open keyboard
                val inputMethodManager =
                    Btn.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(Btn, InputMethodManager.SHOW_IMPLICIT)

                val newText = Btn.text.toString()
                Btn.setText(newText)
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
     * Add a new goal to the database.
     */
    @SuppressLint("SetTextI18n")
    private fun addGoal(
        dateBtn: Button, amountBtn: EditText,
        nameBtn: EditText
    ): Int {

        val recDate = dateBtn.text.toString()
        val recAmount = amountBtn.text.toString()
        val recName = nameBtn.text.toString()

        val dbHandler: AppDB = AppDB(requireContext())
        val existingGoals = dbHandler.viewGoals()
        if(existingGoals.none { it.name == recName }){
            if (recAmount.isNotEmpty() and recName.isNotEmpty()) { // && catName != "None"
                val newRecord = GoalModel(
                    0, recName, recAmount.toFloat(), recDate, 0.0f, 0
                )
                val status = dbHandler.addGoal(newRecord)

                if (status > -1) {
                    // Show a success message and clear the input fields
                    Toast.makeText(requireContext(), "Record saved", Toast.LENGTH_LONG).show()
                    amountBtn.text.clear()
                    nameBtn.text.clear()
                    return 1
                }
            } else {
                // Show an error message if any field is blank
                Toast.makeText(
                    requireContext(),
                    "All fields must be filled in",
                    Toast.LENGTH_LONG
                ).show()
                return 0
            }
        } else {
            // Show an error message if the name already exists
            Toast.makeText(
                requireContext(),
                "This name already exists",
                Toast.LENGTH_LONG
            ).show()
            return 0
        }
        return 0
    }
}
