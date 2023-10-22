package com.example.journalapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.journalapp.MainActivity
import com.example.journalapp.R

class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val textSeeAll = view.findViewById<TextView>(R.id.textSeeAll)

        val textIncome = view.findViewById<TextView>(R.id.textIncome)
        val textExpence = view.findViewById<TextView>(R.id.textExpence)
        val textBalance = view.findViewById<TextView>(R.id.textBalance)

        textSeeAll.setOnClickListener {
           val mainActivity = requireActivity() as MainActivity
            mainActivity.changeTab(1)
        }

        textIncome.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeTab(2)
        }

        textExpence.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeTab(2)
        }

        textBalance.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val balanceInfoFragment = BalanceInfoFragment()

            fragmentTransaction.add(R.id.main_container, balanceInfoFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }




        return view
    }

}