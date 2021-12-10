package com.example.splashscreen.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentsAdapter(manager: FragmentManager, behaviorResumeOnlyCurrentFragment: Int): FragmentPagerAdapter(manager, behaviorResumeOnlyCurrentFragment) {
    private val fragmentArrayList: ArrayList<Fragment> = ArrayList()
    private val fragmentTitleArrayList: ArrayList<String> = ArrayList()

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentArrayList.add(fragment)
        fragmentTitleArrayList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleArrayList[position]
    }

}