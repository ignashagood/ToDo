package nktns.todo.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.R
import nktns.todo.databinding.FragmentArchiveBinding
import nktns.todo.task.list.TaskListFragment
import nktns.todo.task.list.TaskListMode

class ArchiveFragment : Fragment() {

    var binding: FragmentArchiveBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentArchiveBinding.inflate(inflater, container, false).run {
            binding = this
            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            clearBtn.setOnClickListener {
                ArchiveConfirmDeleteFragment().show(childFragmentManager, "confirm delete")
            }
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.beginTransaction()
            .replace(
                R.id.task_list_fragment_container,
                TaskListFragment.newInstance(TaskListMode.Archive)
            )
            .addToBackStack(null)
            .commit()
    }
}
