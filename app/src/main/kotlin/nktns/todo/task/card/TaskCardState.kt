package nktns.todo.task.card

import java.util.Date

sealed class TaskCardState {
    object InitialLoading : TaskCardState()

    data class Content(
        val name: String,
        val isCompleted: Boolean,
        val actionName: String,
        val canDelete: Boolean,
        val completionDate: Date,
    ) : TaskCardState()
}
