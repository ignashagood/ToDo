package nktns.todo.task.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nktns.todo.databinding.TaskCardFragmentBinding
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

    private val viewModel: TaskCardVM by viewModel {
        parametersOf(requireArguments().getParcelable(TASK_CARD_MODE_KEY))
    }

    private var binding: TaskCardFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.action.observe(this) {
            if (it != null) {
                when (it) {
                    TaskCardAction.DISMISS -> dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        TaskCardFragmentBinding.inflate(inflater, container, false).run {
            binding = this
            checkButton.setOnClickListener { viewModel.onButtonClicked() }
            editText.addTextChangedListener { viewModel.onTaskNameChanged(it?.toString().orEmpty()) }
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                TaskCardState.InitialLoading -> binding?.run {
                    checkButton.isVisible = false
                    editText.isVisible = false
                }
                is TaskCardState.Content -> binding?.run {
                    if (editText.text.toString() != state.name) {
                        editText.setText(state.name)
                    }
                    editText.isVisible = true
                    checkText.text = state.actionName
                    checkButton.isVisible = true
                }
            }
        }
    }
}
