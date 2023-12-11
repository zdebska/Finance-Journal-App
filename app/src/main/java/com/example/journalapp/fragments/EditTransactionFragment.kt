package com.example.journalapp.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
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
import com.example.journalapp.R
import com.example.journalapp.models.AppDB
import com.example.journalapp.models.TransactionModel
import java.util.Calendar

class EditTransactionFragment : Fragment() {

    companion object {
        private const val ARG_DATA = "data"

        fun newInstance(data: TransactionModel): EditTransactionFragment {
            val fragment = EditTransactionFragment()
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
        val view = inflater.inflate(R.layout.fragment_edit_transaction, container, false)

        val record: TransactionModel? = arguments?.getParcelable(ARG_DATA)


        val expBtn = view.findViewById<Button>(R.id.selectExpTransactions_e)
        val incBtn = view.findViewById<Button>(R.id.selectIncTransactions_e)

        val whiteClr = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        val greyClr = ColorStateList.valueOf(Color.parseColor("#FFDEDEDE"))

        val arrowBackBtn = view.findViewById<ImageView>(R.id.arrowBackBtn_e)

        val dateBtn = view.findViewById<Button>(R.id.SelectDateBtn_e)
        val dateImg = view.findViewById<ImageView>(R.id.SelectDateImg)

        val amountBtn = view.findViewById<EditText>(R.id.SelectAmountBtn_e)
        val amountImg = view.findViewById<ImageView>(R.id.SelectAmountImg)

        val categoryBtn = view.findViewById<Button>(R.id.SelectCategoryBtn)
        val categoryImg = view.findViewById<ImageView>(R.id.SelectCategoryImg)

        val noteBtn = view.findViewById<EditText>(R.id.SelectNoteBtn)
        val noteImg = view.findViewById<ImageView>(R.id.SelectNoteImg)

        val saveBtn = view.findViewById<Button>(R.id.saveTransactionBtn_e)
        val delBtn = view.findViewById<Button>(R.id.deleteTransactionBtn)

        // Get the current date
        val calendar = Calendar.getInstance()


        dateBtn.text = record?.creationDate
        amountBtn.setText(record?.amount.toString())
        noteBtn.setText(record?.note)

        //TODO: add category reading from a record

        // set colors of the transaction type buttons depending on the record transaction type
        if (record?.transType == "Expense") {

            expBtn.backgroundTintList = whiteClr
            incBtn.backgroundTintList = greyClr
        } else {
            expBtn.backgroundTintList = greyClr
            incBtn.backgroundTintList = whiteClr
        }
        // set listener to the date button to open calendar onclick
        dateSelector(dateBtn, calendar, dateImg)

        //set entered amount of money by a user to the "Amount" field
        setTextVal(amountBtn, amountImg)

        //set entered note by a user to the "Note" field
        setTextVal(noteBtn, noteImg)

        //set white color to the background of the Expense button
        expBtn.setOnClickListener() {
            expBtn.backgroundTintList = whiteClr
            incBtn.backgroundTintList = greyClr
        }
        //set white color to the background of the Income button
        incBtn.setOnClickListener() {
            incBtn.backgroundTintList = whiteClr
            expBtn.backgroundTintList = greyClr
        }

        // update edited transaction and save to DB
        saveBtn.setOnClickListener() {
            val status = editRecord(view, dateBtn, amountBtn, categoryBtn, noteBtn, record)
            if (status == 1) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        delBtn.setOnClickListener() {
            val status = delRecord(record)
            if (status == 1) {
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
    private fun editRecord(
        view: View, dateBtn: Button, amountBtn: EditText, categoryBtn: Button,
        noteBtn: EditText, record: TransactionModel?
    ): Int {
        val expBtn = view.findViewById<Button>(R.id.selectExpTransactions_e)
        val whiteClr =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))

        val recTransType: String =
            if (expBtn.backgroundTintList == whiteClr) {
                "Expense"
            } else {
                "Income"
            }

        val recDate = dateBtn.text.toString()
        val recAmount = amountBtn.text.toString()
        val catName = categoryBtn.text.toString()
        val recNote = noteBtn.text.toString()

        val dbHandler: AppDB = AppDB(requireContext())

        val recCategory = dbHandler.getCategoryId(catName)

        if (recAmount.isNotEmpty()) { // && catName != "None"
            val editedRecord = TransactionModel(
                0, recAmount.toFloat(), recNote, recDate,
                recCategory, recTransType
            )
            val status = dbHandler.updateTransaction(editedRecord, record!!.id)

            if (status > -1) {
                Toast.makeText(requireContext(), "Record saved", Toast.LENGTH_LONG).show()
                amountBtn.text.clear()
                noteBtn.text.clear()
                categoryBtn.text = "None"
                return 1
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Amount or category cannot be blank",
                Toast.LENGTH_LONG
            ).show()
            return 0
        }
        return 0
    }


    private fun delRecord(record: TransactionModel?): Int {
        val dbHandler: AppDB = AppDB(requireContext())
        return dbHandler.deleteTransaction(record!!.id)
    }


}


