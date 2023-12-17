package com.example.journalapp.fragments

import SharedViewModel
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journalapp.R
import com.example.journalapp.fragments.adapters.CategoryAdapter
import com.example.journalapp.fragments.adapters.ColorAdapter
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.CategoryModel
import com.example.journalapp.models.GoalModel
import com.example.journalapp.models.TransactionModel
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditGoalFragment : Fragment(){

    private val sharedViewModel: SharedViewModel by lazy {
        SharedViewModel.getInstance(requireActivity().application)
    }
    private var goal : GoalModel? = null;
    companion object {
        private const val ARG_DATA = "data"

        fun newInstance(data: GoalModel): EditGoalFragment {
            val fragment = EditGoalFragment()
            val args = Bundle()
            args.putParcelable(ARG_DATA, data)
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
        val view = inflater.inflate(R.layout.fragment_edit_goal, container, false)


        val record: GoalModel? = arguments?.getParcelable(ARG_DATA)

        val dbHandler: AppDB = AppDB(requireContext())

        val nameGoal = view.findViewById<EditText>(R.id.SelectNameBtn)
        val amountGoal = view.findViewById<EditText>(R.id.SelectAmountBtn)
        val endDateGoal = view.findViewById<Button>(R.id.SelectDateBtn)
        val arrowBackBtn = view.findViewById<ImageView>(R.id.arrowBackBtn)
        val saveBtn = view.findViewById<Button>(R.id.saveGoalBtn)
        val dateImg = view.findViewById<ImageView>(R.id.SelectDateImg)
        val amountImg = view.findViewById<ImageView>(R.id.SelectAmountImg)
        val nameImg = view.findViewById<ImageView>(R.id.SelectNameImg)

        // Set the goal name to the Button or TextView as needed
        nameGoal.setText(record!!.name.toString())
        amountGoal.setText(record.amount.toString())
        endDateGoal.text = record.endDate
        // Get the current date
        val calendar = Calendar.getInstance()
        // set listener to the date button to open calendar onclick
        dateSelector(endDateGoal, calendar, dateImg)

        //set entered amount of money by a user to the "Amount" field
        setTextVal(amountGoal, amountImg)

        //set entered note by a user to the "Note" field
        setTextVal(nameGoal, nameImg)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Adjust the pattern accordingly
        val endDate = dateFormat.parse(record.endDate)


        // Set the time components of currentDate to midnight
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val currentDateWithoutTime = Date(calendar.timeInMillis)
        if ((record!!.isReached == 0) and (currentDateWithoutTime > endDate)){
            endDateGoal.setTextColor(Color.RED) // Set text color to red
        }

        saveBtn.setOnClickListener() {
            val status = editGoal(view, endDateGoal, amountGoal, nameGoal, record.saved, record.isReached, record.id)
            if (status != null) {
                sharedViewModel.selectGoal(status)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        arrowBackBtn.setOnClickListener {
            // pop the fragment from the back stack to return to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }


    // set date selector to be shown on date button click
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

    private fun setTextVal(Btn: EditText, Img: ImageView) {
        if (Btn.id == R.id.SelectAmountBtn) {
            Btn.inputType = InputType.TYPE_CLASS_NUMBER
        }
        for (i in listOf(Btn, Img)) {
            i.setOnClickListener() {
                // open keyboard
                Btn.requestFocus()
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

    @SuppressLint("SetTextI18n")
    private fun editGoal(
        view: View, dateBtn: Button, amountBtn: EditText,
        nameBtn: EditText, recSaved: Float, recIsReached: Int, recordId: Int
    ): GoalModel? {

        val recDate = dateBtn.text.toString()
        val recAmount = amountBtn.text.toString()
        val recName = nameBtn.text.toString()

        val dbHandler: AppDB = AppDB(requireContext())

        if (recAmount.isNotEmpty()) {
            val editedRecord = GoalModel(
                recordId, recName, recAmount.toFloat(), recDate,
                recSaved, recIsReached
            )
            val status = dbHandler.updateGoal(editedRecord)
            Log.i("MyApp", status.toString());
            if (status > -1) {
                Toast.makeText(requireContext(), "Record saved", Toast.LENGTH_LONG).show()
                amountBtn.text.clear()
                nameBtn.text.clear()
                return editedRecord
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Amount or category cannot be blank",
                Toast.LENGTH_LONG
            ).show()
            return null
        }
        return null
    }

}