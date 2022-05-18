package nktns.todo.base.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import nktns.todo.R
import java.util.Calendar
import java.util.Date

const val REQUEST_KEY = "PICKED_DATE"

class DatePickerFragment : DialogFragment() {

    companion object {
        private const val DATE = "date_key"
        fun newInstance(date: Date): DatePickerFragment {
            return DatePickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(DATE, date)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pickedDate = Calendar.getInstance()
        val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            pickedDate.set(Calendar.YEAR, year)
            pickedDate.set(Calendar.MONTH, month)
            pickedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setFragmentResult(REQUEST_KEY, Bundle().apply { putSerializable(REQUEST_KEY, pickedDate) })
        }
        val date: Date = requireArguments().getSerializable(DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        return DatePickerDialog(
            requireContext(),
            R.style.PickerTheme,
            datePickerListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
    }
}
