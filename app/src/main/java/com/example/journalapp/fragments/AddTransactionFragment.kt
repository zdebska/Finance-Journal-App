package com.example.journalapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.journalapp.R

class AddTransactionFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_transaction, container, false)

        val arrowBackBtn = view.findViewById<ImageView>(R.id.arrowBackBtn)

        arrowBackBtn.setOnClickListener {
            // Pop the fragment from the back stack to return to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }


        return view
    }


}