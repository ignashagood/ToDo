package com.example.todoexample.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.todoexample.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TaskCardFragment : BottomSheetDialogFragment() {

    companion object {
        private const val TASK_CARD_MODE_KEY = "task_card_mode_key"
        fun newInstance(taskCardMode: TaskCardMode): TaskCardFragment {
            return TaskCardFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TASK_CARD_MODE_KEY, taskCardMode)
                }
            }
        }
    }

    private val viewModel: TaskCardViewModel by viewModel {
        parametersOf(arguments!!.getParcelable(TASK_CARD_MODE_KEY))
    }

    private lateinit var checkButton: Button
    private lateinit var editText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val taskCardMode: TaskCardMode = arguments!!.getParcelable(TASK_CARD_MODE_KEY)!!
        val view = inflater.inflate(R.layout.task_card_fragment, container, false)
        checkButton = view.findViewById(R.id.checkButton)
        editText = view.findViewById(R.id.editText)
        when (taskCardMode) {
            is TaskCardMode.Create -> {
                checkButton.text = "Add"
            }
            is TaskCardMode.View -> {
                checkButton.text = "Save"
            }
        }
        viewModel.taskCardAction.observe(this) {
            when (it) {
                TaskCardAction.DISMISS -> dismiss()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.task.observe(viewLifecycleOwner) { task ->
            editText.setText(task.name)
            checkButton.setOnClickListener {
                val taskName: String = editText.text.toString()
                viewModel.onButtonClicked(taskName)
            }
        }
    }
}
