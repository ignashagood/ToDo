package nktns.todo.task.card

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import nktns.todo.R
import nktns.todo.base.NotifyWorker
import nktns.todo.base.NotifyWorker.Companion.NOTIFICATION_ID
import nktns.todo.base.NotifyWorker.Companion.NOTIFICATION_SUBTITLE
import nktns.todo.base.NotifyWorker.Companion.NOTIFICATION_TITLE
import nktns.todo.base.NotifyWorker.Companion.NOTIFICATION_WORK
import nktns.todo.base.format
import nktns.todo.base.illegalState
import nktns.todo.base.pickers.DatePickerFragment
import nktns.todo.base.pickers.PickedDate
import nktns.todo.base.pickers.PickedTime
import nktns.todo.base.pickers.TimePickerFragment
import nktns.todo.catalog.editor.CatalogEditorFragment
import nktns.todo.catalog.editor.CatalogEditorMode
import nktns.todo.catalog.list.SHOW_CATALOG_CREATOR
import nktns.todo.catalog.picker.CatalogPickerAdapter
import nktns.todo.data.database.entity.CatalogEntity
import nktns.todo.data.database.entity.TaskEntity
import nktns.todo.databinding.FragmentTaskCardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit

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
                    is TaskCardAction.ScheduleNotification -> prepareNotification(it.task)
                    is TaskCardAction.DismissCatalogPicker -> catalogPicker?.dismiss()
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
            description.addTextChangedListener { viewModel.onTaskDescriptionChanged(it?.toString().orEmpty()) }
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.let { it ->
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
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
                        if (description.text.toString() != state.description) {
                            description.setText(state.description)
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
        view.findViewById<LinearLayout>(R.id.add_catalog).setOnClickListener {
            CatalogEditorFragment.newInstance(CatalogEditorMode.Create)
                .show(childFragmentManager, SHOW_CATALOG_CREATOR)
            catalogPicker?.dismiss()
        }
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

    private fun prepareNotification(task: TaskEntity) {
        val currentTime = currentTimeMillis()
        val data = Data.Builder()
            .putInt(NOTIFICATION_ID, task.id)
            .putString(NOTIFICATION_TITLE, task.name)
            .putString(NOTIFICATION_SUBTITLE, "Задача посрана - ${task.completionDate.format("d MMMM HH:mm")}")
            .build()
        if (task.completionDate.time > currentTime) {
            val delay = task.completionDate.time - currentTime
            scheduleNotification(delay, data)
        }
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.REPLACE, notificationWork).enqueue()
    }
}
