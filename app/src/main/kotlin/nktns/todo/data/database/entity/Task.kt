package nktns.todo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskId") val id: Int,
    @ColumnInfo(name = "taskParentId") val parentId: Int,
    @ColumnInfo(name = "taskName") val name: String,
    val description: String,
    val creationDate: Date,
    @ColumnInfo(name = "taskCompletionDate") val completionDate: Date,
    val isCompleted: Boolean
)
