package nktns.todo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

const val TASK_LIST_ID = "taskListId"
const val TASK_LIST_NAME = "taskListName"

@Entity(tableName = "taskLists")
data class Catalog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TASK_LIST_ID) val id: Int,
    @ColumnInfo(name = TASK_LIST_NAME) val name: String,
    val creationDate: Date
)
