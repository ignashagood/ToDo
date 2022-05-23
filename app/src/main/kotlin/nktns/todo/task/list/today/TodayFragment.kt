package nktns.todo.task.list.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.R
import nktns.todo.databinding.FragmentTodayBinding
import nktns.todo.task.list.TaskListFragment
import nktns.todo.task.list.TaskListMode

class TodayFragment : Fragment() {

    private var binding: FragmentTodayBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTodayBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(
                    R.id.task_list_today_fragment_container,
                    TaskListFragment.newInstance(
                        TaskListMode.Today
                    )
                )
                .addToBackStack(null)
                .commit()
        }
    }
}
