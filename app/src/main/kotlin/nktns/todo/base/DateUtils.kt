package nktns.todo.base

import nktns.todo.base.pickers.PickedDate
import nktns.todo.base.pickers.PickedTime
import nktns.todo.task.card.LOCALE
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.withoutTime(): Date =
    Calendar.getInstance().run {
        time = this@withoutTime
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        time
    }

fun Date.toPickedDate(): PickedDate =
    Calendar.getInstance().run {
        time = this@toPickedDate
        PickedDate(
            get(Calendar.YEAR),
            get(Calendar.MONTH),
            get(Calendar.DAY_OF_MONTH)
        )
    }

fun Date.toPickedTime(): PickedTime =
    Calendar.getInstance().run {
        time = this@toPickedTime
        PickedTime(
            get(Calendar.HOUR_OF_DAY),
            get(Calendar.MINUTE)
        )
    }

fun Date.applyPickedDate(pickedDate: PickedDate): Date =
    Calendar.getInstance().run {
        time = this@applyPickedDate
        set(Calendar.YEAR, pickedDate.year)
        set(Calendar.MONTH, pickedDate.month)
        set(Calendar.DAY_OF_MONTH, pickedDate.day)
        time
    }

fun Date.applyPickedTime(pickedTime: PickedTime): Date =
    Calendar.getInstance().run {
        this.time = this@applyPickedTime
        set(Calendar.HOUR_OF_DAY, pickedTime.hour)
        set(Calendar.MINUTE, pickedTime.minute)
        this.time
    }

fun formatDate(date: Date): String {
    val formattedDate = DateFormat.getDateInstance(DateFormat.LONG, Locale(LOCALE)).format(date)
    val splitDate = formattedDate.split(" ")
    return "${splitDate[0]} ${splitDate[1]}"
}
