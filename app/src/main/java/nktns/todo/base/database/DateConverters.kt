package nktns.todo.base.database

import androidx.room.TypeConverter
import java.util.Date

class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let(::Date)

    @TypeConverter
    fun dateToTimestamp(date: Date?) = date?.time
}
