package nktns.todo.base

import nktns.todo.base.pickers.PickedDate
import nktns.todo.base.pickers.PickedTime
import java.util.Calendar
import java.util.Date

fun Date.withoutTime(): Date =
    Calendar.getInstance().run {
        time = this@withoutTime
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        time
    }

fun Date.toPickedDate(): PickedDate {
    val cal = Calendar.getInstance()
    cal.time = this
    return PickedDate(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    )
}

fun Date.toPickedTime(): PickedTime {
    val cal = Calendar.getInstance()
    cal.time = this
    return PickedTime(
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE)
    )
}

fun PickedDate.toDate(date: Date): Date {
    return Calendar.getInstance().run {
        time = date
        set(Calendar.YEAR, this@toDate.year)
        set(Calendar.MONTH, this@toDate.month)
        set(Calendar.DAY_OF_MONTH, this@toDate.day)
        time
    }
}

fun PickedTime.toDate(time: Date): Date {
    return Calendar.getInstance().run {
        this.time = time
        set(Calendar.HOUR_OF_DAY, this@toDate.hour)
        set(Calendar.MINUTE, this@toDate.minute)
        this.time
    }
}
