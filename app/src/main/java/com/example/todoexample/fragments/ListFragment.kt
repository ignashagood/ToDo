package com.example.todoexample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.todoexample.ViewModelList
import com.example.todoexample.adapters.TaskAdapter
import com.example.todoexample.database.entity.TaskEntity
import com.example.todoexample.databinding.FragmentListBinding


class ListFragment : Fragment() {

    interface TaskActionHandler {
        fun onTaskDeleteClick(task: TaskEntity)
        fun onTaskCompleted(task: TaskEntity)
    }

    interface TaskActionHandler {
        fun onTaskDeleteClick(task: TaskEntity)
        fun onTaskCompleted(task: TaskEntity)
    }

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
                val task = TaskEntity(result, 0, false)
                viewModel.addTask(task)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.allTasks.observe(viewLifecycleOwner) { adapter.updateList(it) }
    }

    //TODO - diffutil в viewmodel / transform livedata / flow
}