package nktns.todo.main

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import nktns.todo.R
import nktns.todo.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val myViewPager2: ViewPager2 by lazy { binding!!.pager }
    private val myAdapter: ViewPagerFragmentAdapter by lazy {
        ViewPagerFragmentAdapter(childFragmentManager, lifecycle)
    }
    private var binding: FragmentMainBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).run {
        binding = this
        myViewPager2.adapter = myAdapter
        myViewPager2.isUserInputEnabled = false
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout = binding!!.tab
        TabLayoutMediator(tabLayout, myViewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_item_1)
                1 -> tab.text = getString(R.string.tab_item_2)
                2 -> tab.text = getString(R.string.tab_item_3)
            }
        }.attach()
        tabLayout.getTabAt(tabLayout.selectedTabPosition)?.setTextStyle(Typeface.BOLD)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.setTextStyle(Typeface.BOLD)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.setTextStyle(Typeface.NORMAL)
            }

            override fun onTabReselected(tab: TabLayout.Tab) = Unit
        })
    }

    private fun TabLayout.Tab.setTextStyle(style: Int) {
        this.view.children.find { it is TextView }?.let { it ->
            (it as TextView).setTypeface(null, style)
        }
    }
}
