package nktns.todo.base.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import nktns.todo.R

class DatePickerFragment : DialogFragment() {

    companion object {
        const val PICKED_DATE_KEY = "picked_date_key"
        const val RESULT_KEY = "date_picker_result_key"

        fun newInstance(date: PickedDate): DatePickerFragment {
            return DatePickerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PICKED_DATE_KEY, date)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val pickedDate = PickedDate(year, month, dayOfMonth)
            setFragmentResult(RESULT_KEY, Bundle().apply { putParcelable(PICKED_DATE_KEY, pickedDate) })
        }
        val date: PickedDate = requireArguments().getParcelable<PickedDate>(PICKED_DATE_KEY)!!
        return DatePickerDialog(
            requireContext(),
            R.style.PickerTheme,
            datePickerListener,
            date.year,
            date.month,
            date.day
        )
    }
}
