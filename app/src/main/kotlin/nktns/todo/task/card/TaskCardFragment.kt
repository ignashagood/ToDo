package nktns.todo.task.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nktns.todo.R
import nktns.todo.base.pickers.DatePickerFragment
import nktns.todo.base.pickers.REQUEST_KEY
import nktns.todo.base.pickers.TimePickerFragment
import nktns.todo.databinding.TaskCardFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val LOCALE = "ru"

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

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.action.observe(this) {
            if (it != null) {
                when (it) {
                    is TaskCardAction.Dismiss -> dismiss()
                    is TaskCardAction.ShowDatePicker -> showDatePicker(it.date)
                    is TaskCardAction.ShowTimePicker -> showTimePicker(it.time)
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
            saveAddButton.setOnClickListener { viewModel.onSaveAddButtonClicked() }
            deleteButton.setOnClickListener { viewModel.onDeleteButtonClicked() }
            datePickButton.setOnClickListener { viewModel.onDateButtonClicked() }
            timePickButton.setOnClickListener { viewModel.onTimeButtonClicked() }
            name.addTextChangedListener { viewModel.onTaskNameChanged(it?.toString().orEmpty()) }
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
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
                    val catalogName = state.catalogName
                    if (catalogName == "") {
                        catalogText.text = getString(R.string.catalog_name_string_bottom_sheet)
                    } else {
                        catalogText.text = state.catalogName
                    }
                }
            }
        }
    }

    private fun formatDate(date: Date): String {
        val formattedDate = DateFormat.getDateInstance(DateFormat.LONG, Locale(LOCALE)).format(date)
        val splitDate = formattedDate.split(" ")
        return "${splitDate[0]} ${splitDate[1]}"
    }

    private fun showDatePicker(date: Date) {
        DatePickerFragment.newInstance(date).show(childFragmentManager, "DATE_PICKER")
        childFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            this
        ) { _, bundle -> viewModel.onDatePicked(bundle.getSerializable("PICKED_DATE") as Calendar) }
    }

    private fun showTimePicker(time: Date) {
        TimePickerFragment.newInstance(time).show(childFragmentManager, "TIME_PICKER")
        childFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            this
        ) { _, bundle -> viewModel.onTimePicked(bundle.getSerializable("PICKED_TIME") as Calendar) }
    }
}
