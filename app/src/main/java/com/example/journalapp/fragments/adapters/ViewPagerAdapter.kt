/*
* @author Assatulla Dias (xassat00)
* @brief Implementation of a custom class ViewPagerAdapter
* */
@file:Suppress("DEPRECATION")

package com.example.journalapp.fragments.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList


// Custom class to manage a collection of fragments
class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
    FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    // Return the number of fragments in the list
    override fun getCount(): Int {
        return mFragmentList.size
    }

    // Return the fragment at the specified position
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    // Return the title of the fragment at the specified position
    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    // Custom function that adds a fragment and it's title to the Lists
    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

}