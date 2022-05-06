package nktns.todo.base.database.subset

import androidx.room.ColumnInfo
import nktns.todo.base.database.entity.TASK_LIST_ID
import nktns.todo.base.database.entity.TASK_LIST_NAME

data class TaskListWithCounts(
    @ColumnInfo(name = TASK_LIST_ID) val id: Int,
    @ColumnInfo(name = TASK_LIST_NAME) val name: String,
    val taskCount: Int,
    val outdatedTaskCount: Int
)
