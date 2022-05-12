package nktns.todo.base

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
