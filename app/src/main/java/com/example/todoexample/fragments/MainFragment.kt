package com.example.todoexample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.todoexample.adapters.ViewPagerFragmentAdapter
import com.example.todoexample.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    lateinit var myViewPager2: ViewPager2
    lateinit var myAdapter: ViewPagerFragmentAdapter
    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        myViewPager2 = binding.pager
        myAdapter = ViewPagerFragmentAdapter(childFragmentManager, lifecycle)
        myViewPager2.adapter = myAdapter
        myViewPager2.isUserInputEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout = binding.tab
        TabLayoutMediator(tabLayout, myViewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "TaskList"
                1 -> tab.text = "Today"
                2 -> tab.text = "TaskList"
            }
        }.attach()
    }

}