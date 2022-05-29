package nktns.todo.main

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import nktns.todo.R
import nktns.todo.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).run {
        binding = this
        pager.adapter = ViewPagerFragmentAdapter(childFragmentManager, lifecycle)
        pager.isUserInputEnabled = false
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            TabLayoutMediator(tab, pager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.tab_item_1)
                    1 -> tab.text = getString(R.string.tab_item_2)
                    2 -> tab.text = getString(R.string.tab_item_3)
                }
            }.attach()
            tab.getTabAt(tab.selectedTabPosition)?.setTextStyle(Typeface.BOLD)
            tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab) {
                    tab.setTextStyle(Typeface.BOLD)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tab.setTextStyle(Typeface.NORMAL)
                }

                override fun onTabReselected(tab: TabLayout.Tab) = Unit
            })
        }
    }

    private fun TabLayout.Tab.setTextStyle(style: Int) {
        this.view.children.find { it is TextView }?.let { it ->
            (it as TextView).setTypeface(null, style)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.run {
            pager.adapter = null
            binding = null
        }
    }
}
