package nktns.todo.task.card

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import nktns.todo.R
import nktns.todo.base.format
import nktns.todo.base.illegalState
import nktns.todo.base.pickers.DatePickerFragment
import nktns.todo.base.pickers.PickedDate
import nktns.todo.base.pickers.PickedTime
import nktns.todo.base.pickers.TimePickerFragment
import nktns.todo.catalog.picker.CatalogPickerAdapter
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.databinding.FragmentTaskCardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

const val LOCALE = "ru"
const val DATE_PICKER_FRAGMENT_TAG = "date_picker_fragment_tag"
const val TIME_PICKER_FRAGMENT_TAG = "time_picker_fragment_tag"

class TaskCardFragment :
    BottomSheetDialogFragment(),
    CatalogPickerAdapter.OnItemClickListener {

    private var catalogPicker: PopupWindow? = null

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
                    is TaskCardAction.ShowCatalogPicker -> showCatalogPicker(it.catalogs)
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
            catalogPickButton.setOnClickListener { viewModel.onCatalogButtonClicked() }
            name.addTextChangedListener { viewModel.onTaskNameChanged(it?.toString().orEmpty()) }
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is TaskCardState.InitialLoading -> binding?.run {
                        checkButton.isVisible = false
                        name.isVisible = false
                    }
                    is TaskCardState.Content -> binding?.run {
                        if (name.text.toString() != state.name) {
                            name.setText(state.name)
                        }
                        deleteButton.isVisible = state.canDelete
                        name.isVisible = true
                        dateText.text = state.completionDate.format("d MMMM")
                        timeText.text = state.completionDate.format("HH:mm")
                        checkText.text = state.actionName
                        checkButton.isVisible = true
                        catalogText.text = state.catalogName
                    }
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    fun prepareCatalogPicker(catalogs: List<CatalogEntity>): PopupWindow {
        val popupInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = popupInflater.inflate(R.layout.fragment_catalog_picker, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_catalogs)
        val adapter = CatalogPickerAdapter(catalogs, this)
        recyclerView.adapter = adapter
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        return PopupWindow(view, view.measuredWidth, view.measuredHeight)
    }

    private fun showCatalogPicker(catalogs: List<CatalogEntity>) {
        val xOffset = requireContext().resources.getDimensionPixelSize(R.dimen.catalog_picker_x_offset)
        val yOffset = requireContext().resources.getDimensionPixelSize(R.dimen.catalog_picker_y_offset)
        catalogPicker = prepareCatalogPicker(catalogs)
        catalogPicker?.isOutsideTouchable = true
        catalogPicker?.isFocusable = true
        catalogPicker?.showAsDropDown(this.view, xOffset, -yOffset)
    }

    private fun showDatePicker(date: PickedDate) {
        DatePickerFragment.newInstance(date).show(childFragmentManager, DATE_PICKER_FRAGMENT_TAG)
    }

    private fun showTimePicker(time: PickedTime) {
        TimePickerFragment.newInstance(time).show(childFragmentManager, TIME_PICKER_FRAGMENT_TAG)
    }

    override fun onItemClick(catalog: CatalogEntity) {
        viewModel.onCatalogPicked(catalog)
        catalogPicker?.dismiss()
    }
}
