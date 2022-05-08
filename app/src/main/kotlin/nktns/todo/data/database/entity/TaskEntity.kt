package nktns.todo.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taskItems")
data class TaskEntity(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val itemId: Int,
    val isCompleted: Boolean
)
