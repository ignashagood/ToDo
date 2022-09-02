package nktns.todo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.lang.System.currentTimeMillis
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
    @ColumnInfo(name = "taskCatalogId") val catalogId: Int?,
) {
    @Ignore
    val isOutdated: Boolean = completionDate.time <= currentTimeMillis()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (creationDate != other.creationDate) return false
        if (completionDate != other.completionDate) return false
        if (isCompleted != other.isCompleted) return false
        if (catalogId != other.catalogId) return false
        if (isOutdated != other.isOutdated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + completionDate.hashCode()
        result = 31 * result + isCompleted.hashCode()
        result = 31 * result + (catalogId ?: 0)
        result = 31 * result + isOutdated.hashCode()
        return result
    }
}
