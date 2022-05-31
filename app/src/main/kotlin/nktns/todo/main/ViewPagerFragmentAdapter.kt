package nktns.todo.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import nktns.todo.catalog.list.CatalogListFragment
import nktns.todo.task.list.TaskListFragment
import nktns.todo.task.list.TaskListMode
import nktns.todo.task.list.today.TodayFragment

class ViewPagerFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            1 -> TaskListFragment.newInstance(TaskListMode.All)
            2 -> CatalogListFragment()
            else -> error("Unexpected position $position")
        }
    }
}
