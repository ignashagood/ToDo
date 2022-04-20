package com.example.todoexample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.todoexample.R
import com.example.todoexample.Task
import com.example.todoexample.ViewModelList
import com.example.todoexample.adapters.Loading
import com.example.todoexample.adapters.State
import com.example.todoexample.adapters.Success
import com.example.todoexample.adapters.TaskAdapter
import com.example.todoexample.databinding.FragmentListBinding

interface TaskActionHandler {
    fun onTaskCompleted(task: Task)
}

class ListFragment : Fragment() {

    private val viewModel: ViewModelList by viewModels()
    private lateinit var adapter: TaskAdapter
    private lateinit var binding: FragmentListBinding
    private val bottomSheetFragment = TaskBottomDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TaskAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter
        binding.addButton.setOnClickListener {
            bottomSheetFragment.show(childFragmentManager, "BottomSheetDialog")
        }
        childFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            val result = bundle.getString("bundleKey")
            if (result != null) {
                viewModel.addNewTask(result)
            }
        }
        return binding.root
    }

    fun updateView(state: State) {
        when (state) {
            is Loading -> {}
            is Success -> {
                if (binding.recyclerView.isComputingLayout) {
                    binding.recyclerView.post {
                        adapter.updateList(state.taskList, state.diffResult)
                    }
                } else adapter.updateList(state.taskList, state.diffResult)
            }
        }
    }                                                                                               //TODO Спросить у Семена

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(viewLifecycleOwner, object : Observer<State> {
            override fun onChanged(t: State) {
                updateView(t)
            }
        })
    }

}