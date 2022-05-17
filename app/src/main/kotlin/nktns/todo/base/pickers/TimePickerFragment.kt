package nktns.todo.base.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import nktns.todo.R
import java.util.Calendar
import java.util.Date

class TimePickerFragment : DialogFragment() {

    companion object {
        private const val TIME = "time_key"
        fun newInstance(time: Date): TimePickerFragment {
            return TimePickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(TIME, time)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pickedTime = Calendar.getInstance()
        val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            pickedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            pickedTime.set(Calendar.MINUTE, minute)
            setFragmentResult(REQUEST_KEY, Bundle().apply { putSerializable("PICKED_TIME", pickedTime) })
        }
        val time: Date = requireArguments().getSerializable(TIME) as Date
        val calendar = Calendar.getInstance()
        calendar.time = time
        return TimePickerDialog(
            requireContext(),
            R.style.PickerTheme,
            timePickerListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }
}
