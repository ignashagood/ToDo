package nktns.todo.task.list.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.R
import nktns.todo.base.formatDate
import nktns.todo.databinding.FragmentTodayBinding
import nktns.todo.task.card.LOCALE
import nktns.todo.task.list.TaskListFragment
import nktns.todo.task.list.TaskListMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        binding?.run {
            dayOfMonth.text = formatDate(Date())
            dayOfWeek.text = SimpleDateFormat("EEEE", Locale(LOCALE)).format(Date())
        }
    }
}
