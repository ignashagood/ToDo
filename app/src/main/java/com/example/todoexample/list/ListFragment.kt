package com.example.todoexample.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todoexample.base.database.entity.TaskEntity
import com.example.todoexample.card.TaskCardFragment
import com.example.todoexample.card.TaskCardMode
import com.example.todoexample.databinding.FragmentListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment(), TaskAdapter.OnItemClickListener {

    interface TaskActionHandler {
        fun onTaskDeleteClick(task: TaskEntity)
        fun onTaskCompleted(task: TaskEntity)
    }

    private val viewModel by viewModel<ViewModelList>()
    private val adapter: TaskAdapter by lazy { TaskAdapter(viewModel, this) }
    private var binding: FragmentListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding?.apply {
            recyclerView.adapter = adapter
            addButton.setOnClickListener {
                TaskCardFragment.newInstance(TaskCardMode.Create)
                    .show(childFragmentManager, "CreateSheetDialog")
            }
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.allTasks.observe(viewLifecycleOwner) { adapter?.updateList(it) }
    }

    override fun onItemClick(task: TaskEntity) {
        TaskCardFragment.newInstance(TaskCardMode.View(task.itemId))
            .show(childFragmentManager, "ChangeSheetDialog")
    }
}
