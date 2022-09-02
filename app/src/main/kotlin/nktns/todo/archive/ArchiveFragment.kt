package nktns.todo.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.R
import nktns.todo.base.AppEvent
import nktns.todo.base.EventBus
import nktns.todo.databinding.FragmentArchiveBinding
import nktns.todo.task.list.TaskListFragment
import nktns.todo.task.list.TaskListMode
import org.koin.java.KoinJavaComponent

class ArchiveFragment : Fragment() {

    var binding: FragmentArchiveBinding? = null
    private val eventBus: EventBus = KoinJavaComponent.getKoin().get()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentArchiveBinding.inflate(inflater, container, false).run {
            binding = this
            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            clearBtn.setOnClickListener {
                eventBus.emitEvent(AppEvent.ClearArchive)
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
