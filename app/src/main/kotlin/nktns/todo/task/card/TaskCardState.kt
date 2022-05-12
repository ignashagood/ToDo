package nktns.todo.task.card

sealed class TaskCardState {
    object InitialLoading : TaskCardState()

    data class Content(val name: String, val isCompleted: Boolean, val actionName: String) : TaskCardState()
}