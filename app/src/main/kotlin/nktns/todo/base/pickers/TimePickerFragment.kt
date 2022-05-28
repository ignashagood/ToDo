package nktns.todo.base.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import nktns.todo.R

class TimePickerFragment : DialogFragment() {

    companion object {
        const val PICKED_TIME_KEY = "picked_time_key"
        const val RESULT_KEY = "time_picker_result_key"

        fun newInstance(time: PickedTime): TimePickerFragment {
            return TimePickerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PICKED_TIME_KEY, time)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val pickedTime = PickedTime(hourOfDay, minute)
            setFragmentResult(RESULT_KEY, Bundle().apply { putParcelable(PICKED_TIME_KEY, pickedTime) })
        }
        val time: PickedTime = requireArguments().getParcelable(PICKED_TIME_KEY)!!
        return TimePickerDialog(
            requireContext(),
            R.style.PickerTheme,
            timePickerListener,
            time.hour,
            time.minute,
            true
        )
    }
}
