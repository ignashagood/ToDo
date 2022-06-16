package nktns.todo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "taskId") val id: Int,
    @ColumnInfo(name = "taskName") val name: String,
    @ColumnInfo(name = "taskDescription") val description: String,
    @ColumnInfo(name = "taskCreationDate") val creationDate: Date,
    @ColumnInfo(name = "taskCompletionDate") val completionDate: Date,
    @ColumnInfo(name = "taskIsCompleted") val isCompleted: Boolean,
    @ColumnInfo(name = "taskCatalogId") val catalogId: Int?
)
