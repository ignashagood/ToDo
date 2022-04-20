package com.example.todoexample.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoexample.fragments.ListFragment
import com.example.todoexample.fragments.TodayFragment

class ViewPagerFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ListFragment()
            }
            1 -> {
                TodayFragment()
            }
            2 -> {
                ListFragment()
            }
            else -> {
                Fragment()
            }
        }
    }

}