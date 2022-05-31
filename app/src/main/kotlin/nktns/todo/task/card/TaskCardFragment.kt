package nktns.todo.task.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import nktns.todo.R
import nktns.todo.base.formatDate
import nktns.todo.base.illegalState
import nktns.todo.base.pickers.DatePickerFragment
import nktns.todo.base.pickers.PickedDate
import nktns.todo.base.pickers.PickedTime
import nktns.todo.base.pickers.TimePickerFragment
import nktns.todo.databinding.FragmentTaskCardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.DateFormat
import java.util.Locale

const val LOCALE = "ru"
const val DATE_PICKER_FRAGMENT_TAG = "date_picker_fragment_tag"
const val TIME_PICKER_FRAGMENT_TAG = "time_picker_fragment_tag"

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

    private var binding: FragmentTaskCardBinding? = null

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.action.collect {
                when (it) {
                    is TaskCardAction.Dismiss -> dismiss()
                    is TaskCardAction.ShowDatePicker -> showDatePicker(it.date)
                    is TaskCardAction.ShowTimePicker -> showTimePicker(it.time)
                }
            }
        }
        childFragmentManager.setFragmentResultListener(
            DatePickerFragment.RESULT_KEY,
            this
        ) { _, bundle ->
            bundle.getParcelable<PickedDate>(DatePickerFragment.PICKED_DATE_KEY).let {
                if (it != null) {
                    viewModel.onDatePicked(it)
                } else {
                    illegalState("Unexpected input picked date")
                }
            }
        }
        childFragmentManager.setFragmentResultListener(
            TimePickerFragment.RESULT_KEY,
            this
        ) { _, bundle ->
            bundle.getParcelable<PickedTime>(TimePickerFragment.PICKED_TIME_KEY).let {
                if (it != null) {
                    viewModel.onTimePicked(it)
                } else {
                    illegalState("Unexpected input picked time")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentTaskCardBinding.inflate(inflater, container, false).run {
            binding = this
            saveAddButton.setOnClickListener { viewModel.onSaveAddButtonClicked() }
            deleteButton.setOnClickListener { viewModel.onDeleteButtonClicked() }
            datePickButton.setOnClickListener { viewModel.onDateButtonClicked() }
            timePickButton.setOnClickListener { viewModel.onTimeButtonClicked() }
            name.addTextChangedListener { viewModel.onTaskNameChanged(it?.toString().orEmpty()) }
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    TaskCardState.InitialLoading -> binding?.run {
                        checkButton.isVisible = false
                        name.isVisible = false
                    }
                    is TaskCardState.Content -> binding?.run {
                        if (name.text.toString() != state.name) {
                            name.setText(state.name)
                        }
                        deleteButton.isVisible = state.canDelete
                        name.isVisible = true
                        dateText.text = formatDate(state.completionDate)
                        timeText.text =
                            DateFormat.getTimeInstance(DateFormat.SHORT, Locale(LOCALE)).format(state.completionDate)
                        checkText.text = state.actionName
                        checkButton.isVisible = true
                        catalogText.text = state.catalogName
                    }
                }
            }
        }
    }

    private fun showDatePicker(date: PickedDate) {
        DatePickerFragment.newInstance(date).show(childFragmentManager, DATE_PICKER_FRAGMENT_TAG)
    }

    private fun showTimePicker(time: PickedTime) {
        TimePickerFragment.newInstance(time).show(childFragmentManager, TIME_PICKER_FRAGMENT_TAG)
    }
}
