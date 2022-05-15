package nktns.todo.task.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.data.database.entity.TaskEntity
import nktns.todo.databinding.FragmentListBinding
import nktns.todo.task.card.TaskCardFragment
import nktns.todo.task.card.TaskCardMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskListFragment : Fragment(), TaskAdapter.OnItemClickListener {

    interface TaskActionHandler {
        fun onTaskCompleted(task: TaskEntity)
    }

    private val viewModel by viewModel<TaskListVM>()
    private val adapter: TaskAdapter by lazy { TaskAdapter(viewModel, this) }
    private var binding: FragmentListBinding? = null
    private var contentStateApplied: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding?.apply {
            recyclerViewTasks.adapter = adapter
            addButton.setOnClickListener {
                TaskCardFragment.newInstance(TaskCardMode.Create).show(childFragmentManager, "CreateSheetDialog")
            }
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is TaskListState.InitialLoading -> {}
                is TaskListState.Content -> applyState(it)
            }
        }
    }

    override fun onItemClick(task: TaskEntity) {
        TaskCardFragment.newInstance(TaskCardMode.View(task.id)).show(childFragmentManager, "ChangeSheetDialog")
    }

    private fun applyState(state: TaskListState.Content) {
        if (contentStateApplied) {
            adapter.updateList(state.taskList, state.diffResult)
        } else {
            // Если состояние ещё ни разу не было применено, значит список мы просто резко отображаем, без анимаций
            adapter.initList(state.taskList)
            contentStateApplied = true
        }
    }
}
