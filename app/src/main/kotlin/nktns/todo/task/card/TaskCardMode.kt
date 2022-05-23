package nktns.todo.task.card

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class TaskCardMode : Parcelable {
    @Parcelize
    data class Create(val catalogId: Int) : TaskCardMode()

    @Parcelize
    data class View(val taskId: Int) : TaskCardMode()
}
