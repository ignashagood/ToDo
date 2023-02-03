package nktns.todo.main

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import nktns.todo.R
import nktns.todo.archive.ArchiveFragment
import nktns.todo.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null

    private var optionsWindow: PopupWindow? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).run {
        binding = this
        pager.adapter = ViewPagerFragmentAdapter(childFragmentManager, lifecycle)
        pager.isUserInputEnabled = true
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            optionsBtn.setOnClickListener {
                showOptionsWindow()
            }
            TabLayoutMediator(tab, pager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.tab_item_today)
                    1 -> tab.text = getString(R.string.tab_item_all)
                    2 -> tab.text = getString(R.string.tab_item_catalogs)
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

    private fun prepareOptionsWindow(): PopupWindow {
        val popupInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = popupInflater.inflate(R.layout.options_window, null)
        view.findViewById<LinearLayout>(R.id.archive).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, ArchiveFragment())
                .addToBackStack(null)
                .commit()
            optionsWindow?.dismiss()
        }
        return PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun showOptionsWindow() {
        optionsWindow = prepareOptionsWindow()
        val yOffset = requireContext().resources.getDimensionPixelSize(R.dimen.menu_y_offset)
        val xOffset = requireContext().resources.getDimensionPixelSize(R.dimen.menu_x_offset)
        optionsWindow?.isOutsideTouchable = true
        optionsWindow?.isFocusable = true
        optionsWindow?.showAsDropDown(view, xOffset, yOffset, Gravity.TOP)
    }

}
