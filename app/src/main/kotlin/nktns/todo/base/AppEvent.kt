package nktns.todo.base

sealed class AppEvent {
    object UpdateTaskList : AppEvent()
    object ClearArchive : AppEvent()
}
