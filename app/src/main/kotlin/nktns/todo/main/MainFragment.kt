package nktns.todo.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
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
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        myViewPager2.adapter = myAdapter
        myViewPager2.isUserInputEnabled = false
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout = binding!!.tab
        TabLayoutMediator(tabLayout, myViewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "все"
                1 -> tab.text = "сегодня"
                2 -> tab.text = "списки дел"
            }
        }.attach()
    }
}
