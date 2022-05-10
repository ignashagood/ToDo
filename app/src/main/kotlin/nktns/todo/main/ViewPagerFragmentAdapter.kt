package nktns.todo.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import nktns.todo.list.TaskListFragment
import nktns.todo.list.TodayFragment

class ViewPagerFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TaskListFragment()
            1 -> TodayFragment()
            2 -> TaskListFragment()
            else -> error("Unexpected position $position")
        }
    }
}
