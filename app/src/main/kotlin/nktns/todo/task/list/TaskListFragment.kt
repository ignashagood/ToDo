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
import org.koin.core.parameter.parametersOf

class TaskListFragment : Fragment(), TaskAdapter.OnItemClickListener {

    interface TaskActionHandler {
        fun onTaskCompleted(task: TaskEntity)
    }

    companion object {
        private const val TASK_LIST_MODE_KEY = "task_list_mode_key"
        fun newInstance(taskListMode: TaskListMode): TaskListFragment {
            return TaskListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TASK_LIST_MODE_KEY, taskListMode)
                }
            }
        }
    }

    private val viewModel: TaskListVM by viewModel {
        parametersOf(
            requireArguments().getParcelable(TASK_LIST_MODE_KEY)
        )
    }
    private val adapter: TaskAdapter by lazy { TaskAdapter(viewModel, this) }
    private var binding: FragmentListBinding? = null
    private var contentStateApplied: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.action.observe(this) {
            if (it != null) {
                when (it) {
                    is TaskListAction.ShowCreateBottomSheet ->
                        TaskCardFragment.newInstance(TaskCardMode.Create(it.catalogId))
                            .show(childFragmentManager, "ChangeSheetDialog")
                    is TaskListAction.ShowViewBottomSheet ->
                        TaskCardFragment.newInstance(TaskCardMode.View(it.taskId))
                            .show(childFragmentManager, "ChangeSheetDialog")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentListBinding.inflate(inflater, container, false).run {
        binding = this
        recyclerViewTasks.adapter = adapter
        addButton.setOnClickListener {
            viewModel.onAddButtonClicked()
        }
        root
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
        viewModel.onItemClicked(task)
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
